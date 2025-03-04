package engsoft.matfit.view.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import engsoft.matfit.R
import engsoft.matfit.model.AlunoDTO
import engsoft.matfit.model.AlunoRequest
import engsoft.matfit.model.AlunoResponse
import engsoft.matfit.model.AlunoUpdate
import engsoft.matfit.service.repository.AlunoRepository
import engsoft.matfit.util.EstadoRequisicao
import kotlinx.coroutines.launch
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream

class AlunoViewModel : ViewModel() {

    private val repository = AlunoRepository()

    private val _cadastro = MutableLiveData<Boolean>()
    val cadastro: LiveData<Boolean> = _cadastro

    private val _deletar = MutableLiveData<Boolean?>()
    val deletar: LiveData<Boolean?> = _deletar

    private val _buscarAluno = MutableLiveData<AlunoResponse?>()
    val buscarAluno: LiveData<AlunoResponse?> = _buscarAluno

    private val _atualizarAluno = MutableLiveData<AlunoResponse?>()
    val atualizarAluno: LiveData<AlunoResponse?> = _atualizarAluno

    private val _realizarPagamento = MutableLiveData<Boolean>()
    val realizarPagamento: LiveData<Boolean> = _realizarPagamento

    private val _verificarPagamento = MutableLiveData<AlunoResponse?>()
    val verificarPagamento: LiveData<AlunoResponse?> = _verificarPagamento

    private val _estadoRequisicao = MutableLiveData<EstadoRequisicao<List<AlunoDTO>>>()
    val estadoRequisicao: LiveData<EstadoRequisicao<List<AlunoDTO>>> = _estadoRequisicao

    fun listarAlunos() {
        _estadoRequisicao.postValue(EstadoRequisicao.Carregando())

        viewModelScope.launch {
            try {
                val response = repository.listarAlunos()

                if (response.isNotEmpty())
                    _estadoRequisicao.postValue(EstadoRequisicao.Sucesso(response))
                else
                    _estadoRequisicao.postValue(EstadoRequisicao.Sucesso(emptyList()))
            } catch (e: Exception) {
                _estadoRequisicao.postValue(EstadoRequisicao.Erro("Erro ao buscar aluno!"))
            }
        }
    }

    fun cadastrarAluno(aluno: AlunoRequest) {
        viewModelScope.launch {
            _cadastro.postValue(repository.cadastrarAluno(aluno))
        }
    }

    fun deletarAluno(cpf: String) {
        viewModelScope.launch {
            _deletar.postValue(repository.deletarAluno(cpf))
            listarAlunos()
        }
    }

    fun reseteDeletar() {
        _deletar.postValue(null)
    }

    fun buscarAluno(cpf: String) {
        viewModelScope.launch {
            try {
                _buscarAluno.postValue(repository.buscarAluno(cpf))
            } catch (e: Exception) {
                _buscarAluno.postValue(null)
                e.printStackTrace()
            }
        }
    }

    fun atualizarAluno(cpf: String, aluno: AlunoUpdate) {
        viewModelScope.launch {
            try {
                _atualizarAluno.postValue(repository.atualizarAluno(cpf, aluno))
            } catch (e: Exception) {
                _atualizarAluno.postValue(null)
                e.printStackTrace()
            }
        }
    }

    fun realizarPagamento(cpf: String) {
        viewModelScope.launch {
            _realizarPagamento.postValue(repository.realizarPagamento(cpf))
        }
    }

    fun verificarPagamento(cpf: String) {
        viewModelScope.launch {
            _verificarPagamento.postValue(repository.verificarPagamento(cpf))
        }
    }

    // responsável por Exportar os dados em formato excel
    fun exportarAlunosParaExcel(
        listAlunos: List<AlunoDTO>,
        salvarAquivoLauncher: ActivityResultLauncher<String>
    ) {
        salvarAquivoLauncher.launch("Alunos_Cadastrados.xlsx")

    }

    // responsável por fazer a criação e inserção dos alunos na tabela a ser exportada
    fun escreverExcel(uri: Uri, context: Context, listAlunos: List<AlunoDTO>) {
        try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Alunos")

            val cabecalho = sheet.createRow(0)
            cabecalho.createCell(0).setCellValue("cpf")
            cabecalho.createCell(1).setCellValue("nome")
            cabecalho.createCell(2).setCellValue("esporte")
            cabecalho.createCell(3).setCellValue("data do Pagamento")
            cabecalho.createCell(4).setCellValue("pagamento em dia")

            listAlunos.forEachIndexed { index, aluno ->
                val linha = sheet.createRow(index + 1)
                linha.createCell(0).setCellValue(aluno.cpf)
                linha.createCell(1).setCellValue(aluno.nome)
                linha.createCell(2).setCellValue(aluno.esporte)
                linha.createCell(3).setCellValue(aluno.dataPagamento)
                linha.createCell(4)
                    .setCellValue(if (aluno.pagamentoAtrasado) "Pendente" else "Pago")

            }

            context.contentResolver.openOutputStream(uri)?.use { outputStream: OutputStream ->
                workbook.write(outputStream)
                workbook.close()
            }

            abrirArquivo(uri, context)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // responsável por abrir o arquivo exportado após o download no dispositivo 
    //  -> faz uma verificação no sistema para saber se existe algum aplicativo para abri-lo
    private fun abrirArquivo(uri: Uri, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else
            Toast.makeText(context, context.getString(R.string.textAppNotFound), Toast.LENGTH_SHORT)
                .show()
    }
}
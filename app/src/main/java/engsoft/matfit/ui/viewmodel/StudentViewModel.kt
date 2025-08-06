package engsoft.matfit.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import engsoft.matfit.R
import engsoft.matfit.events.StudentEvent
import engsoft.matfit.model.Student
import engsoft.matfit.model.StudentRequestDTO
import engsoft.matfit.model.StudentResponseDTO
import engsoft.matfit.model.StudentUpdateDTO
import engsoft.matfit.service.RetrofitService
import engsoft.matfit.service.StudentService
import engsoft.matfit.service.repository.AlunoRepository
import engsoft.matfit.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream

class StudentViewModel : ViewModel() {

    private val repository = AlunoRepository(RetrofitService.getService(StudentService::class.java))

    private val _update = MutableStateFlow<StudentState>(StudentState())
    val updateState: Flow<StudentState> = _update

    data class StudentState(
        val register: Boolean = false,
        val delete: Boolean = false,
        val update: StudentResponseDTO? = StudentResponseDTO(),
        val getStudent: StudentResponseDTO? = StudentResponseDTO(),
        val doPayment: Boolean = false,
        val verifyPayment: StudentResponseDTO? = StudentResponseDTO(),
        val requestState: RequestState<List<Student>> = RequestState.Carregando()
    )

    private fun updateUiState(update: (StudentState) -> StudentState){
        _update.update(update)
    }

    fun onEvent(event: StudentEvent){
        when (event){
            StudentEvent.OnGetAllStudents -> getAllStudents()
            StudentEvent.OnBackPressed -> onBackPressed()
            is StudentEvent.OnAddStudent -> registerStudent(event.student)
            is StudentEvent.OnDeleteStudent -> deleteStudent(event.cpf)
            is StudentEvent.OnGetStudentByCpf -> getStudent(event.cpf)
            is StudentEvent.OnUpdateStudent -> updateStudent(event.cpf, event.student)
            is StudentEvent.OnDoPayment -> doPayment(event.cpf)
            is StudentEvent.OnVerifyPayment -> verifyPayment(event.cpf)
        }
    }

    private fun registerStudent(student: StudentRequestDTO){
        viewModelScope.launch {
            val result = repository.cadastrarAluno(student)
            updateUiState { it.copy(register = result) }
        }
    }

    private fun verifyPayment(cpf: String){
        viewModelScope.launch {
            val result = repository.verificarPagamento(cpf)
            updateUiState { it.copy(verifyPayment = result) }
        }
    }

    private fun doPayment(cpf: String){
        viewModelScope.launch {
            val result = repository.realizarPagamento(cpf)
            updateUiState { it.copy(doPayment = result) }
        }
    }

    private fun updateStudent(cpf: String, student: StudentUpdateDTO){
        viewModelScope.launch {
            val result = repository.atualizarAluno(cpf, student)
            updateUiState { it.copy(update = result) }
        }
    }

    private fun getStudent(cpf: String){
        viewModelScope.launch {
            val result = repository.buscarAluno(cpf)
            updateUiState { it.copy(getStudent = result) }
        }
    }


    private fun deleteStudent(cpf: String){
        viewModelScope.launch {
           val result = repository.deletarAluno(cpf)
            updateUiState { it.copy(delete = result) }
        }
    }

    private fun onBackPressed(){
        StudentState()
    }

    private fun getAllStudents() {
        updateUiState { it.copy(requestState = RequestState.Carregando()) }
        viewModelScope.launch {
            try {
                val response = repository.listarAlunos()

                if (response.isNotEmpty())
                    updateUiState { it.copy(requestState = RequestState.Sucesso(response)) }
                else
                    updateUiState { it.copy(requestState = RequestState.Sucesso(emptyList())) }
            } catch (e: Exception) {
                updateUiState { it.copy(requestState = RequestState.Erro("List is empty!")) }
            }
        }
    }

    // responsável por fazer a criação e inserção dos alunos na tabela a ser exportada
    fun escreverExcel(uri: Uri, context: Context, listAlunos: List<Student>) {
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
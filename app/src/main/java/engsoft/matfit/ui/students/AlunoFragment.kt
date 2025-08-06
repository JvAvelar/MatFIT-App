package engsoft.matfit.view.alunos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import engsoft.matfit.R
import engsoft.matfit.databinding.FragmentAlunoBinding
import engsoft.matfit.listener.OnStudentListener
import engsoft.matfit.model.Student
import engsoft.matfit.util.Constants
import engsoft.matfit.util.RequestState
import engsoft.matfit.view.alunos.adapter.AlunoAdapter
import engsoft.matfit.view.viewmodel.AlunoViewModel

@SuppressLint("SetTextI18n")
class AlunoFragment : Fragment() {
    private var _binding: FragmentAlunoBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this)[AlunoViewModel::class.java]
    }

    private val adapter = AlunoAdapter()

    private var listaAlunosExcel = emptyList<Student>()

    private val salvarArquivoLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    ) { uri ->
        uri?.let {
            viewModel.escreverExcel(it, requireContext(), listaAlunosExcel)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDownload.setOnClickListener {
            fazerDownloadArquivo()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAlunoBinding.inflate(inflater, container, false)

        // Layout e Adapter
        binding.recyclerListAlunos.layoutManager = LinearLayoutManager(context)
        binding.recyclerListAlunos.adapter = adapter

        // Mostra o texto ao passar o dedo no botão de download
        TooltipCompat.setTooltipText(binding.btnDownload, "Exportar dados")

        val listener = object : OnStudentListener {
            override fun onUpdate(cpf: String) {
                atualizarAluno(cpf)
                Log.i("info_BtnOnUpdate", "sucesso ao cadastrar aluno!")
            }

            override fun onDelete(cpf: String) {
                deletarAluno(cpf)
                Log.i("info_BtnOnDelete", "sucesso ao deletar aluno!")
            }

            override fun OnPayment(cpf: String) {
                verificarPagamento(cpf)
                Log.i("info_BtnOnPayment", "sucesso ao realizar pagamento!")
            }
        }

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(context, AddAlunoActivity::class.java))
        }

        viewModel.listarAlunos()
        adapter.attachListener(listener)
        observadores()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.listarAlunos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observadores() {
        viewModel.deletar.observe(viewLifecycleOwner) { sucesso ->
            when (sucesso) {
                false -> {
                    Log.i("info_deletar", "sucesso ao deletar aluno! -> $sucesso")
                    toast(getString(R.string.textSucessDeletedAluno))
                }

                true -> {
                    Log.i("info_deletar", "falha ao deletar aluno! -> $sucesso")
                    toast(getString(R.string.textFailureDeletedAluno))
                }

                else -> Log.i("info_deletar", "ERRO: valor nulo! -> $sucesso")
            }
        }

        // responsável por lista os alunos cadastrados e carregar corretamente os estados da barra de progresso
        viewModel.estadoRequisicao.observe(viewLifecycleOwner) { estado ->
            when (estado) {
                is RequestState.Carregando -> {
                    mostrarCarregamento()
                    Log.i("info_EstadoRequisicaoCarregando", "Carregando os dados!!")
                }

                is RequestState.Sucesso -> {
                    val alunos = estado.data
                    adapter.updateAlunos(alunos)
                    listaAlunosExcel = alunos
                    mostrarCardModel()
                    Log.i("info_EstadoRequisicaoSucesso", "Sucesso no listar Alunos! -> $alunos ")
                }

                is RequestState.Erro -> {
                    mostrarMsgListaVazia()
                    Log.i(
                        "info_EstadoRequisicaoErro",
                        "Erro ao listar alunos! -> ${estado.mensagem} "
                    )
                }

                else -> mostrarMsgListaVazia()
            }
        }
    }

    private fun fazerDownloadArquivo() {
        viewModel.estadoRequisicao.value?.let { estado ->
            when (estado) {
                is RequestState.Carregando -> {
                    binding.ProgressBar.visibility = View.VISIBLE
                }

                is RequestState.Sucesso -> {
                    Log.i("info_btnDownload", "fazer Download do arquivo")
                    listaAlunosExcel = estado.data
                    viewModel.exportarAlunosParaExcel(listaAlunosExcel, salvarArquivoLauncher)
                }

                is RequestState.Erro -> {
                    toast(estado.mensagem)
                }
            }
        }
    }

    private fun deletarAluno(cpf: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.deleteAluno))
            .setMessage(getString(R.string.textConfirmationDelete))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deletarAluno(cpf)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create().show()
    }

    private fun verificarPagamento(cpf: String) {
        viewModel.verificarPagamento(cpf)
        viewModel.verificarPagamento.observe(viewLifecycleOwner) { aluno ->
            aluno?.let {
                Log.i("info_OnPaymentAluno", "Operação bem-sucedida -> $it")
                val intent = Intent(context, PagamentoAlunoActivity::class.java).apply {
                    putExtra(Constants.Student.CPF, it.cpf)
                    putExtra(Constants.Student.NOME, it.nome)
                    putExtra(Constants.Student.ESPORTE, it.esporte)
                    putExtra(Constants.Student.DATA_PAGAMENTO, it.dataPagamento)
                    putExtra(Constants.Student.PAGAMENTO_ATRASADO, it.pagamentoAtrasado)
                }
                startActivity(intent)
            } ?: toast(getString(R.string.textAlunoNotFound))
        }
    }

    private fun atualizarAluno(cpf: String) {
        viewModel.buscarAluno(cpf)
        viewModel.buscarAluno.observe(viewLifecycleOwner) { aluno ->
            Log.i("info_clickAtualizar", "observeBuscarAluno -> $aluno")
            aluno?.let {
                val intent = Intent(context, UpdateAlunoActivity::class.java).apply {
                    putExtra(Constants.Student.CPF, it.cpf)
                    putExtra(Constants.Student.NOME, it.nome)
                    putExtra(Constants.Student.ESPORTE, it.esporte)
                }
                startActivity(intent)
            } ?: toast(getString(R.string.textAlunoNotFound))
        }
    }

    private fun mostrarCarregamento() {
        binding.ProgressBar.visibility = View.VISIBLE
        binding.emptyView.visibility = View.VISIBLE
        binding.recyclerListAlunos.visibility = View.GONE
    }

    private fun mostrarCardModel() {
        binding.recyclerListAlunos.visibility = View.VISIBLE
        binding.emptyView.visibility = View.GONE
        binding.ProgressBar.visibility = View.GONE
    }

    private fun mostrarMsgListaVazia() {
        binding.emptyView.visibility = View.VISIBLE
        binding.recyclerListAlunos.visibility = View.GONE
        binding.ProgressBar.visibility = View.GONE
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
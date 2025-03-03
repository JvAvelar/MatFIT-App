package engsoft.matfit.view.funcionarios

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import engsoft.matfit.R
import engsoft.matfit.databinding.FragmentFuncionarioBinding
import engsoft.matfit.listener.OnFuncionarioListener
import engsoft.matfit.util.Constantes
import engsoft.matfit.util.EstadoRequisicao
import engsoft.matfit.view.funcionarios.adapter.FuncionarioAdapter
import engsoft.matfit.view.viewmodel.FuncionarioViewModel

class FuncionarioFragment : Fragment() {

    private var _binding: FragmentFuncionarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FuncionarioViewModel
    private val adapter = FuncionarioAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFuncionarioBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FuncionarioViewModel::class.java]

        binding.recyclerListFuncionario.layoutManager = LinearLayoutManager(context)
        binding.recyclerListFuncionario.adapter = adapter

        val listener = object : OnFuncionarioListener {
            override fun onUpdate(cpf: String) {
                atualizarFuncionario(cpf)
            }

            override fun onDelete(cpf: String) {
                deletarFuncionario(cpf)
            }
        }

        adapter.attachListener(listener)

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(context, AddFuncionarioActivity::class.java))
        }

        viewModel.listarFuncionarios()

        observadores()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.listarFuncionarios()
    }

    override fun onResume() {
        super.onResume()
        viewModel.listarFuncionarios()
    }

    private fun observadores() {
        viewModel.estadoRequisicao.observe(viewLifecycleOwner) { estado ->
            when (estado) {
                is EstadoRequisicao.Carregando -> {
                    mostrarCarregamento()
                    Log.i("info_EstadoRequisicaoCarregando", "Carregando os dados!!")
                }

                is EstadoRequisicao.Sucesso -> {
                    mostrarCardModel()
                    adapter.updateFuncionario(estado.data)
                    Log.i("info_EstadoRequisicaoSucesso", "Sucesso ao listar os funcionarios!!")
                }

                is EstadoRequisicao.Erro -> {
                    mostrarMsgListaVazia()
                    toast(estado.mensagem)
                    Log.i("info_EstadoRequisicaoErro", "Erro ao listar os funcionarios!!")
                }

                else -> mostrarMsgListaVazia()
            }
        }

        // valores invertidos para sucesso
        viewModel.deletarFuncionario.observe(viewLifecycleOwner) { sucesso ->
            when (sucesso) {
                false -> {
                    Log.i("info_deletarFuncionario", "Sucesso ao deletar funcionario! -> $sucesso")
                    toast(getString(R.string.textSuccessDeleteFuncionario))
                    viewModel.reseteDeletar()
                }

                true -> {
                    Log.i("info_deletarFuncionario", "Erro ao deletar funcionario! -> $sucesso")
                    toast(getString(R.string.textFailureDeleteFuncionario))
                    viewModel.reseteDeletar()
                }

                else -> {
                    toast("ERRO: valor nulo!! -> $sucesso")
                    Log.i("info_deletarFuncionario", "ERRO: valor nulo! -> $sucesso")
                }
            }
        }
    }

    private fun atualizarFuncionario(cpf: String) {
        viewModel.buscarFuncionario(cpf)
        viewModel.buscarFuncionario.observe(viewLifecycleOwner) { funcionario ->
            Log.i("info_onUpdateFuncionario", "Operação bem-sucedida -> $funcionario")
            if (funcionario != null) {
                val intent = Intent(context, UpdateFuncionarioActivity::class.java)
                intent.putExtra(Constantes.Funcionario.CPF, funcionario.cpf)
                intent.putExtra(Constantes.Funcionario.NOME, funcionario.nome)
                intent.putExtra(Constantes.Funcionario.FUNCAO, funcionario.funcao)
                intent.putExtra(Constantes.Funcionario.CARGA_HORARIA, funcionario.cargaHoraria)
                startActivity(intent)
            } else {
                Log.i("info_onUpdateFuncionario", "Erro de execução -> $funcionario")
                toast(getString(R.string.textFuncionarioNotFound))
            }
        }
    }

    private fun deletarFuncionario(cpf: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.deleteFuncionario))
            .setMessage(getString(R.string.textConfirmationDelete))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                viewModel.deletarFuncionario(cpf)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which -> null }
            .create()
            .show()
    }

    private fun mostrarCarregamento() {
        binding.ProgressBar.visibility = View.VISIBLE
        binding.emptyView.visibility = View.VISIBLE
        binding.recyclerListFuncionario.visibility = View.GONE
    }

    private fun mostrarMsgListaVazia() {
        binding.emptyView.visibility = View.VISIBLE
        binding.ProgressBar.visibility = View.GONE
        binding.recyclerListFuncionario.visibility = View.GONE

    }

    private fun mostrarCardModel() {
        binding.recyclerListFuncionario.visibility = View.VISIBLE
        binding.ProgressBar.visibility = View.GONE
        binding.emptyView.visibility = View.GONE

    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
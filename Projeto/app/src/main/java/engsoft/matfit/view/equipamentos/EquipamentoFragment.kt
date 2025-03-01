package engsoft.matfit.view.equipamentos

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
import engsoft.matfit.databinding.FragmentEquipamentoBinding
import engsoft.matfit.listener.OnEquipamentoListener
import engsoft.matfit.model.Constantes
import engsoft.matfit.util.EstadoRequisicao
import engsoft.matfit.view.equipamentos.adapter.EquipamentoAdapter
import engsoft.matfit.view.viewmodel.EquipamentoViewModel

class EquipamentoFragment : Fragment() {

    private var _binding: FragmentEquipamentoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EquipamentoViewModel
    private val adapter = EquipamentoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEquipamentoBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[EquipamentoViewModel::class.java]

        binding.recyclerListEquipamento.layoutManager = LinearLayoutManager(context)
        binding.recyclerListEquipamento.adapter = adapter

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(context, AddEquipamentoActivity::class.java))
        }

        val listener = object : OnEquipamentoListener {
            override fun onDelete(id: Int) {
                deletarEquipamento(id)
                Log.i("info_BtnOnDelete", "sucesso ao deletar equipamento!")
            }

            override fun onUpdate(id: Int) {
                atualizarEquipamento(id)
                Log.i("info_BtnOnUpdate", "sucesso ao cadastrar equipamento!")
            }
        }

        adapter.attachListener(listener)

        viewModel.listarEquipamentos()

        observadores()

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        viewModel.listarEquipamentos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observadores() {
        // no deletar os valores são invertidos para sucesso ->
        viewModel.deletar.observe(viewLifecycleOwner) { sucesso ->
            when (sucesso) {
                false -> {
                    Log.i("info_deletarEquipamento", "sucesso ao deletar equipamento! -> $sucesso")
                    toast(getString(R.string.textSuccessDeleteEquipamento))
                    viewModel.reseteDeletar()
                }

                true -> {
                    Log.i("info_deletarEquipamento", "falha ao deletar equipamento! -> $sucesso")
                    toast(getString(R.string.textFailureDeleteEquipamento))
                    viewModel.reseteDeletar()
                }

                else -> {
                    toast("ERRO: valor nulo!! -> $sucesso")
                    Log.i("info_deletar", "ERRO: valor nulo! -> $sucesso")
                }
            }
        }

        // responsável por lista os equipamentos cadastrados e carregar corretamente os estados da barra de progresso
        viewModel.estadoRequisicao.observe(viewLifecycleOwner) { estado ->
            when (estado) {
                is EstadoRequisicao.Carregando -> {
                    mostrarCarregamento()
                    Log.i("info_EstadoRequisicaoCarregando", "Carregando os dados!!")
                }

                is EstadoRequisicao.Sucesso -> {
                    adapter.updateEquipamentos(estado.data)
                    mostrarCardModel()
                    Log.i("info_EstadoRequisicaoSucesso", "Sucesso ao listar os equipamentos!!")
                }

                is EstadoRequisicao.Erro -> {
                    mostrarMsgListaVazia()
                    toast(estado.mensagem)
                    Log.i(
                        "info_EstadoRequisicaoErro",
                        "Erro ao listar os equipamentos!! -> ${estado.mensagem}"
                    )
                }

                else -> mostrarMsgListaVazia()
            }
        }
    }

    private fun deletarEquipamento(id: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.deleteEquipamento))
            .setMessage(getString(R.string.textConfirmationDelete))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deletarEquipamento(id)
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> null }
            .create()
            .show()
    }

    private fun atualizarEquipamento(id: Int) {
        viewModel.buscarEquipamento(id)
        viewModel.buscarEquipamento.observe(viewLifecycleOwner) { equipamento ->
            if (equipamento != null) {
                Log.i("info_onUpdateEquipamento", "Operação bem-sucedida -> $equipamento")
                val intent = Intent(context, UpdateEquipamentoActivity::class.java)
                intent.putExtra(Constantes.Equipamento.ID, equipamento.id)
                intent.putExtra(Constantes.Equipamento.NOME, equipamento.nome)
                intent.putExtra(Constantes.Equipamento.QUANTIDADE, equipamento.quantidade)
                startActivity(intent)
            } else {
                Log.i("info_onUpdateEquipamento", "Erro de execução -> $equipamento")
                toast(getString(R.string.textEquipamentoNotFound))
            }
        }
    }

    private fun mostrarCarregamento() {
        binding.ProgressBar.visibility = View.VISIBLE
        binding.emptyView.visibility = View.VISIBLE
        binding.recyclerListEquipamento.visibility = View.GONE
    }

    private fun mostrarMsgListaVazia() {
        binding.emptyView.visibility = View.VISIBLE
        binding.recyclerListEquipamento.visibility = View.GONE
        binding.ProgressBar.visibility = View.GONE
    }

    private fun mostrarCardModel() {
        binding.recyclerListEquipamento.visibility = View.VISIBLE
        binding.emptyView.visibility = View.GONE
        binding.ProgressBar.visibility = View.GONE
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}

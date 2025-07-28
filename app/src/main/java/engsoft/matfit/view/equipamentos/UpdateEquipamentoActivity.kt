package engsoft.matfit.view.equipamentos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import engsoft.matfit.R
import engsoft.matfit.databinding.ActivityUpdateEquipamentoBinding
import engsoft.matfit.model.EquipamentoDTO
import engsoft.matfit.util.BaseValidacao
import engsoft.matfit.util.Constantes
import engsoft.matfit.view.viewmodel.EquipamentoViewModel

@SuppressLint("SetTextI18n")
class UpdateEquipamentoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUpdateEquipamentoBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[EquipamentoViewModel::class.java]
    }

    private val baseValidacao = BaseValidacao(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            atualizar()
        }
        binding.iconBack.setOnClickListener {
            finish()
        }

        observador()

        dadosPadroes()
    }

    private fun atualizar() {
        val id = intent?.getIntExtra(Constantes.Equipamento.ID, 0) ?: 0
        val nome = binding.editNome.text.toString()
        val quantidade = binding.editQuantidade.text.toString().toInt()

        if (!baseValidacao.validarNome(nome) || nome.isEmpty())
            baseValidacao.toast(getString(R.string.textNameInvalid))
        else if (quantidade < 1)
            baseValidacao.toast(getString(R.string.textValidationQuantity))
        else
            viewModel.atualizarEquipamento(id, EquipamentoDTO(nome = nome, quantidade = quantidade))
    }


    private fun dadosPadroes() {
        val nome = intent?.getStringExtra(Constantes.Equipamento.NOME) ?: ""
        val quantidade = intent?.getIntExtra(Constantes.Equipamento.QUANTIDADE, 0) ?: 0
        binding.editNome.setText(nome)
        binding.editQuantidade.setText(quantidade.toString())
    }

    private fun observador() {
        viewModel.atualizar.observe(this) { equipamento ->
            if (equipamento != null) {
                baseValidacao.toast(getString(R.string.textSuccessUpdateEquipamento))
                finish()
            } else
                baseValidacao.toast(getString(R.string.textFailureUpdateEquipamento))
        }
    }
}
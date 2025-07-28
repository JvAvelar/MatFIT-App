package engsoft.matfit.view.equipamentos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import engsoft.matfit.R
import engsoft.matfit.databinding.ActivityAddEquipamentoBinding
import engsoft.matfit.model.EquipamentoDTO
import engsoft.matfit.util.BaseValidacao
import engsoft.matfit.util.Constantes
import engsoft.matfit.view.viewmodel.EquipamentoViewModel

@SuppressLint("SetTextI18n")
class AddEquipamentoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddEquipamentoBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[EquipamentoViewModel::class.java]
    }

    private val baseValidacao = BaseValidacao(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewModelProvider(this)[EquipamentoViewModel::class.java]

        supportActionBar?.hide()

        binding.editQuantidade.setText(Constantes.Equipamento.DEFAULT_VALUE)

        observador()

        binding.btnRegister.setOnClickListener {
            addEquipamento()
        }
        binding.iconBack.setOnClickListener {
            finish()
        }
    }


    private fun addEquipamento() {
        val nome = binding.editName.text.toString()
        val quantidade = binding.editQuantidade.text.toString().toInt()

        if (!baseValidacao.validarNome(nome) || nome.isEmpty())
            baseValidacao.toast(getString(R.string.textNameInvalid))
        else if (quantidade < 1)
            baseValidacao.toast(getString(R.string.textValidationQuantity))
        else
            viewModel.cadastrarEquipamento(EquipamentoDTO(nome = nome, quantidade = quantidade))
    }

    private fun observador() {
        viewModel.cadastrar.observe(this) {
            if (it) {
                baseValidacao.toast(getString(R.string.textSuccessRegisterEquipamento))
                finish()
            } else
                baseValidacao.toast(getString(R.string.textFailureRegisterEquipamento))
        }
    }
}
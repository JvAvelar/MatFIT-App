package engsoft.matfit.view.funcionarios

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import engsoft.matfit.R
import engsoft.matfit.databinding.ActivityAddFuncionarioBinding
import engsoft.matfit.util.BaseValidacao
import engsoft.matfit.model.FuncionarioDTO
import engsoft.matfit.view.viewmodel.FuncionarioViewModel

class AddFuncionarioActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddFuncionarioBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[FuncionarioViewModel::class.java]
    }

    private val baseValidacao = BaseValidacao(this)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.editCargaHoraria.setText("1")

        observadores()

        click()

    }

    private fun click() {
        binding.btnRegister.setOnClickListener {
            addFuncionario()
        }

        binding.iconBack.setOnClickListener {
            finish()
        }
    }

    private fun addFuncionario() {
        val cpf = binding.editCpf.text.toString()
        val nome = binding.editNome.text.toString()
        val funcao = binding.editFuncao.text.toString()
        val cargaHoraria = binding.editCargaHoraria.text.toString().toInt()

        if (!baseValidacao.validarCpf(cpf))
            baseValidacao.toast(getString(R.string.textErrorCpf))
        else if (!baseValidacao.validarNome(nome))
            baseValidacao.toast(getString(R.string.textErrorName))
        else if (!baseValidacao.validarEsporte(funcao))
            baseValidacao.toast(getString(R.string.textErrorFuncao))
        else if (!baseValidacao.validarCargaHoraria(cargaHoraria))
            baseValidacao.toast(getString(R.string.textValidarCargaHoraria))
        else viewModel.cadastrarFuncionario(FuncionarioDTO(cpf, nome, funcao, cargaHoraria))
    }

    private fun observadores() {
        viewModel.cadastroFuncionario.observe(this) {
            if (it) {
                baseValidacao.toast(getString(R.string.textSuccessRegisterFuncionario))
                finish()
            } else
                baseValidacao.toast(getString(R.string.textFailureRegisterFuncionario))
        }
    }
}
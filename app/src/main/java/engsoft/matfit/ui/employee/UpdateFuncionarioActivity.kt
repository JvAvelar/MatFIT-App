package engsoft.matfit.view.funcionarios

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import engsoft.matfit.R
import engsoft.matfit.databinding.ActivityUpdateFuncionarioBinding
import engsoft.matfit.util.BaseValidation
import engsoft.matfit.model.EmployeeUpdateDTO
import engsoft.matfit.util.Constants
import engsoft.matfit.view.viewmodel.FuncionarioViewModel

class UpdateFuncionarioActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUpdateFuncionarioBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[FuncionarioViewModel::class.java]
    }

    private val baseValidacao = BaseValidation(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {
            finish()
        }
        binding.btnRegister.setOnClickListener {
            atualizar()
        }

        observadores()

        dadosPadroes()
    }

    private fun dadosPadroes() {
        val nome = intent?.getStringExtra(Constants.Employee.NOME) ?: ""
        val funcao = intent?.getStringExtra(Constants.Employee.FUNCAO) ?: ""
        val cargaHoraria = intent?.getIntExtra(Constants.Employee.CARGA_HORARIA, 0).toString()

        binding.editName.setText(nome)
        binding.editFuncao.setText(funcao)
        binding.editCargaHoraria.setText(cargaHoraria)
        Log.i("info_UpdateFuncionarioActiviy -> dadosPadroes", "dadosPadroes: $nome")

    }

    private fun atualizar() {
        val cpf = intent.getStringExtra("cpf") ?: ""
        val nome = binding.editName.text.toString()
        val funcao = binding.editFuncao.text.toString()
        val cargaHoraria = binding.editCargaHoraria.text.toString().toInt()
        val funcionarioUpdate = EmployeeUpdateDTO(nome, funcao, cargaHoraria)

        if (!baseValidacao.validarNome(nome))
            baseValidacao.toast(getString(R.string.textErrorName))
        else if (!baseValidacao.validarEsporte(funcao))
            baseValidacao.toast(getString(R.string.textErrorFuncao))
        else if (!baseValidacao.validarCargaHoraria(cargaHoraria))
            baseValidacao.toast(getString(R.string.textValidarCargaHoraria))
        else viewModel.atualizarFuncionario(cpf, funcionarioUpdate)
    }

    private fun observadores() {
        viewModel.atualizarFuncionario.observe(this) { funcionario ->
            if (funcionario != null) {
                baseValidacao.toast(getString(R.string.textSuccessUpdateFuncionario))
                finish()
            } else
                baseValidacao.toast(getString(R.string.textFailureUpdateFuncionario))
        }
    }
}
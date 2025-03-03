package engsoft.matfit.view.funcionarios

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import engsoft.matfit.R
import engsoft.matfit.databinding.ActivityUpdateFuncionarioBinding
import engsoft.matfit.util.BaseValidacao
import engsoft.matfit.model.FuncionarioUpdate
import engsoft.matfit.util.Constantes
import engsoft.matfit.view.viewmodel.FuncionarioViewModel

class UpdateFuncionarioActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUpdateFuncionarioBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[FuncionarioViewModel::class.java]
    }

    private val baseValidacao = BaseValidacao(this)

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
        val nome = intent?.getStringExtra(Constantes.Funcionario.NOME) ?: ""
        val funcao = intent?.getStringExtra(Constantes.Funcionario.FUNCAO) ?: ""
        val cargaHoraria = intent?.getIntExtra(Constantes.Funcionario.CARGA_HORARIA, 0).toString()

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
        val funcionarioUpdate = FuncionarioUpdate(nome, funcao, cargaHoraria)

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
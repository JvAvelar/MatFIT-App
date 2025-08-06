package engsoft.matfit.view.alunos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import engsoft.matfit.R
import engsoft.matfit.R.drawable.ic_cancel
import engsoft.matfit.R.drawable.ic_done
import engsoft.matfit.databinding.ActivityPagamentoAlunoBinding
import engsoft.matfit.util.BaseValidation
import engsoft.matfit.util.Constants
import engsoft.matfit.view.viewmodel.AlunoViewModel

@SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
class PagamentoAlunoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityPagamentoAlunoBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[AlunoViewModel::class.java]
    }

    private val baseValidacao = BaseValidation(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.btnPagar.setOnClickListener {
            fazerPagamento()
        }

        binding.imageBack.setOnClickListener {
            finish()
        }

        alterarDados()

        observador()

        supportActionBar?.hide()
    }

    private fun alterarDados() {
        val cpf = intent?.getStringExtra(Constants.Student.CPF) ?: ""
        val pagamentoAtrasado = intent?.getBooleanExtra(Constants.Student.PAGAMENTO_ATRASADO, true) ?: true
        val nome = intent?.getStringExtra(Constants.Student.NOME) ?: ""
        val esporte = intent?.getStringExtra(Constants.Student.ESPORTE) ?: ""
        val dataPagamento = intent?.getStringExtra(Constants.Student.DATA_PAGAMENTO) ?: ""

        binding.textDoCpf.text = " ${formatarCpf(cpf)}"
        binding.textDoName.text = " $nome"
        binding.textDoSport.text = " $esporte"
        binding.textDoDay.text = " $dataPagamento"

        Log.i(
            "info_pagamentoAlterarDados",
            "Operação bem sucedida -> $cpf, $nome, $esporte, $dataPagamento, $pagamentoAtrasado"
        )

        if (!pagamentoAtrasado)
            binding.imageStatus.setImageDrawable(getDrawable(ic_done))
        else
            binding.imageStatus.setImageDrawable(getDrawable(ic_cancel))
    }

    private fun formatarCpf(cpf: String): String {
        val cpfNumerico = cpf.replace("[^0-9]".toRegex(), "")
        return if (cpfNumerico.length == 11) {
            "%s.%s.%s-%s".format(
                cpfNumerico.substring(0, 3),
                cpfNumerico.substring(3, 6),
                cpfNumerico.substring(6, 9),
                cpfNumerico.substring(9, 11)
            )
        } else cpf
    }

    private fun fazerPagamento() {
        val cpf = intent?.getStringExtra(Constants.Student.CPF) ?: ""
        val pagamentoAtrasado = intent?.getBooleanExtra(Constants.Student.PAGAMENTO_ATRASADO, true) ?: true

        if (pagamentoAtrasado) {
            viewModel.realizarPagamento(cpf)
        } else {
            baseValidacao.toast(getString(R.string.textMensalidadeOK))
        }
    }

    private fun observador() {
        viewModel.realizarPagamento.observe(this) {
            if (it) {
                baseValidacao.toast(getString(R.string.textSuccessPayment))
                binding.imageStatus.setImageDrawable(getDrawable(ic_done))
            }
            else {
                baseValidacao.toast(getString(R.string.textFailurePayment))
                binding.imageStatus.setImageDrawable(getDrawable(ic_cancel))
            }
        }
    }
}
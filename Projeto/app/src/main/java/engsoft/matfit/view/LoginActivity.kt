package engsoft.matfit.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import engsoft.matfit.R
import engsoft.matfit.databinding.ActivityLoginBinding
import engsoft.matfit.model.BaseValidacao

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val baseValidacao = BaseValidacao(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bntEnter.setOnClickListener {
            doLogin()
        }

        binding.textRegister.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
            finish()
        }

        supportActionBar?.hide()

    }

    override fun onResume() {
        super.onResume()
        verifyUserLogged()
    }

    // Fazer login com Firebase
    private fun doLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPasswd.text.toString()

        if (email.isEmpty() || password.isEmpty() || password.length < 6) {
            baseValidacao.toast(getString(R.string.textEmailAndPasswordIncorrect))
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { success ->
                baseValidacao.toast(getString(R.string.textInformationWelcomeBack))
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                baseValidacao.toast("${exception.message}")
            }
    }

    // Verifica se o usuáro está logado após a criação e envia diretamente para a MainActivity se estiver
    private fun verifyUserLogged() {
        val user = auth.currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else return
    }
}


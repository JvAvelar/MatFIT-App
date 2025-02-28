package engsoft.matfit.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
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

        binding.bntEnter.setOnClickListener { view ->
            fazerLogin(view)
        }

        binding.textRegister.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
            finish()
        }

        supportActionBar?.hide()

    }

    override fun onResume() {
        super.onResume()
        verificarUsuarioLogado()
    }

    // Fazer login com Firebase
    private fun fazerLogin(view: View) {
        val email = binding.editEmail.text.toString()
        val password = binding.editPasswd.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            baseValidacao.snackForError(view, getString(R.string.textEmailAndPasswordIncorrect))
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { success ->
                baseValidacao.toast(getString(R.string.textInformationWelcomeBack))
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                val msgErro = when (exception) {
                    is FirebaseAuthWeakPasswordException -> getString(R.string.textEmailAndPasswordIncorrect)
                    is FirebaseAuthInvalidCredentialsException,
                    is FirebaseAuthEmailException -> getString(R.string.textEmailAndPasswordIncorrect)

                    is FirebaseAuthUserCollisionException -> getString(R.string.textEmailUsed)
                    else -> getString(R.string.textErroUserRegister)
                }
                baseValidacao.snackForError(view, msgErro)
            }
    }

    // Verifica se o usuáro está logado após a criação e envia diretamente para a MainActivity se estiver
    private fun verificarUsuarioLogado() {
        val user = auth.currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else return
    }
}


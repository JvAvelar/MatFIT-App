package engsoft.matfit.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import engsoft.matfit.R
import engsoft.matfit.databinding.ActivityCadastroBinding
import engsoft.matfit.model.BaseValidacao

class CadastroActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val baseValidacao = BaseValidacao(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.bntRegister.setOnClickListener { view ->
            cadastrarUsuario(view)
        }
    }

    // Criação do usuário com Firebase
    private fun cadastrarUsuario(view: View) {
        val email = binding.editEmail.text.toString()
        val password = binding.editPasswd.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            baseValidacao.snackForError(
                view,
                getString(R.string.textInformationEmailAndPasswordMandatory)
            )
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { success ->
                Log.i("info_CadastroActivity", "Sucesso! -> ${success.user}")
                val emailSuccess = success.user?.email
                baseValidacao.toast("$emailSuccess ${getString(R.string.textSuccessRegisterEmail)}")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                Log.i("info_CadastroActivity", "Erro! -> ${exception.message}")
                val msgErro = when (exception) {
                    is FirebaseAuthWeakPasswordException -> getString(R.string.textPasswordLessSixCharacter)
                    is FirebaseAuthInvalidCredentialsException,
                    is FirebaseAuthEmailException -> getString(R.string.textEmailInvalid)

                    is FirebaseAuthUserCollisionException -> getString(R.string.textEmailUsed)
                    else -> getString(R.string.textErroUserRegister)
                }
                baseValidacao.snackForError(view, msgErro)
            }
    }
}
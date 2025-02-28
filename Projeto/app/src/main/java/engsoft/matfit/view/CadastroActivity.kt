package engsoft.matfit.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
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

        binding.bntRegister.setOnClickListener {
            registerUser()
        }
    }

    // Criação do usuário com Firebase
    private fun registerUser() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPasswd.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            baseValidacao.toast(getString(R.string.textInformationEmailAndPasswordMandatory))
            return
        } else if (password.length < 6) {
            baseValidacao.toast(getString(R.string.textPasswordLessSixCharacter))
            return
        } else if (!email.contains("@")) {
            baseValidacao.toast(getString(R.string.textEmailInvalid))
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { success ->
                val emailSuccess = success.user?.email
                // TOAST COM LONGA DURAÇÂO
                Toast.makeText(this, "$emailSuccess ${getString(R.string.textSuccessRegisterEmail)}", Toast.LENGTH_LONG)
                    .show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }.addOnFailureListener { exception ->
                baseValidacao.toast("Erro: ${exception.message}")
            }

    }
}
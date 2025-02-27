package engsoft.matfit.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import engsoft.matfit.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

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
            toast("Email e/ou senha incorretos.")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { success ->
                toast("Seja bem vindo(a) de volta!")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception -> toast("${exception.message}")
               // toast("Email e/ou senha incorretos.")
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

    private fun toast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}


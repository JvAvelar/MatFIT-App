package engsoft.matfit.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import engsoft.matfit.R
import engsoft.matfit.databinding.ActivityCadastroBinding

class CadastroActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

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
            toast("Email e senha obrigatórios para realizar seu cadastro.")
            return
        } else if (password.length < 6) {
            toast("Sua senha deve conter pelo menos 6 caracteres.")
            return
        } else if (!email.contains("@")) {
            toast("Email inválido. Verifique!")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { success ->
                val emailSuccess = success.user?.email
                // TOAST COM LONGA DURAÇÂO
                Toast.makeText(this, "$emailSuccess foi cadastrado com sucesso!", Toast.LENGTH_LONG)
                    .show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }.addOnFailureListener { exception ->
                toast("Erro: ${exception.message}")
            }

    }

    private fun toast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}
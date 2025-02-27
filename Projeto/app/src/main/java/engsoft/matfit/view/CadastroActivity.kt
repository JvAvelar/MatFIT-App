package engsoft.matfit.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import engsoft.matfit.R
import engsoft.matfit.databinding.ActivityCadastroBinding
import engsoft.matfit.service.SharedPreferences

class CadastroActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedPreferences = SharedPreferences(this)
        supportActionBar?.hide()

        binding.bntRegister.setOnClickListener {
            saveName()
            registerUser()

        }
    }

    override fun onStart() {
        super.onStart()
        verifyUserLogged()
    }

    // Verifica se o usuáro está logado após a criação e envia diretamente para a MainActivity se estiver
    private fun verifyUserLogged(){
       val user = auth.currentUser
        if (user != null ){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else
            Toast.makeText(this, "Não tem usuário logado.", Toast.LENGTH_SHORT).show()

    }

    // Criação do usuário com Firebase
    private fun registerUser() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPasswd.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { success ->
                val emailSuccess = success.user?.email
                Toast.makeText(this, "$emailSuccess foi cadastrado com sucesso!", Toast.LENGTH_LONG).show()

            } .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

    }

    // Verifica e salva o nome do usuário com SharedPreferences
    private fun saveName() {
        val name = binding.editName.text.toString()
        if (name.isBlank()) {
            Toast.makeText(this, getString(R.string.textFieldEmptyLogin), Toast.LENGTH_SHORT).show()
            return
        } else if (name.length <= 2)
            Toast.makeText(this, getString(R.string.textNameInvalid), Toast.LENGTH_SHORT).show()
        else {
            sharedPreferences.saveName(name)
        }
    }
}
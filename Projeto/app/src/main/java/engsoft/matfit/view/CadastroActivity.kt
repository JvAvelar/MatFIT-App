package engsoft.matfit.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import engsoft.matfit.R
import engsoft.matfit.databinding.ActivityCadastroBinding
import engsoft.matfit.service.SharedPreferences

class CadastroActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedPreferences = SharedPreferences(this)
        actionBar?.hide()

        binding.bntRegister.setOnClickListener {
            saveName()
            saveEmailAndPassword()

        }
    }

    private fun saveEmailAndPassword() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPasswd.text.toString()

    }

    private fun saveName() {
        val nome = binding.editName.text.toString()
        if (nome.isBlank()) {
            Toast.makeText(this, getString(R.string.textFieldEmptyLogin), Toast.LENGTH_SHORT).show()
            return
        } else if (nome.length <= 2)
            Toast.makeText(this, getString(R.string.textNameInvalid), Toast.LENGTH_SHORT).show()
        else {
            sharedPreferences.saveName(nome)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
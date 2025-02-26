package engsoft.matfit.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import engsoft.matfit.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bntEnter.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.textRegister.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        supportActionBar?.hide()

    }
}


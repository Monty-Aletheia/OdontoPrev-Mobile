package br.com.fiap.challenge.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.com.fiap.challenge.R
import br.com.fiap.challenge.databinding.LoginActivityBinding
import br.com.fiap.challenge.network.LoginDTO
import br.com.fiap.challenge.network.createAuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button = binding.button

        button.setOnClickListener{


        }



    }

    private fun authLogin() =lifecycleScope.launch {
        val registerNumber = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        try {

            val authService = createAuthService()
            val loginDTO = LoginDTO(registerNumber, password)

            lifecycleScope.launch(Dispatchers.IO) {
                val response = authService.signIn(loginDTO)

                if (response.isSuccessful && response.body()?.sessionToken != null) {
                    val sessionToken = response.body()?.sessionToken!!.replace("session-token-", "")
                    val dentistId = sessionToken.let { UUID.fromString(it) }

                    // TODO manter dentistId p uso posterior

                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Não foi possível fazer o login",
                        Toast.LENGTH_LONG
                    ).show()


                }
            }

        } catch (ex: Exception){
            Toast.makeText(
                requireContext(),
                ex.message,
                Toast.LENGTH_LONG
            ).show()
        }


    }
}
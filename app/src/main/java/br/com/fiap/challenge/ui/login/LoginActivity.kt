package br.com.fiap.challenge.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import br.com.fiap.challenge.data.SessionManager
import br.com.fiap.challenge.databinding.LoginActivityBinding
import br.com.fiap.challenge.network.LoginDTO
import br.com.fiap.challenge.network.createAuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

        button.setOnClickListener {
            authLogin()
        }


    }


    private fun authLogin() = lifecycleScope.launch {
        val registerNumber = binding.registrationNumberEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        try {
            val authService = createAuthService()
            val loginDTO = LoginDTO(registerNumber, password)
            val response = authService.signIn(loginDTO)



            if (response.isSuccessful && response.body()?.token != null) {
                val sessionToken = response.body()?.token!!.replace("session-token-", "")
                SessionManager.saveSessionToken(this@LoginActivity, sessionToken)


                val intent = Intent(this@LoginActivity, MenuActivity::class.java)
                startActivity(intent)

            } else {

                Toast.makeText(
                    this@LoginActivity,
                    "Não foi possível fazer o login",
                    Toast.LENGTH_LONG
                ).show()
            }


        } catch (ex: Exception) {
            Toast.makeText(
                this@LoginActivity,
                ex.message,
                Toast.LENGTH_LONG
            ).show()
        }


    }

}
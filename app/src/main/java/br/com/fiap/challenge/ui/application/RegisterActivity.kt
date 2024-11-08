package br.com.fiap.challenge.ui.application

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.com.fiap.challenge.R
import br.com.fiap.challenge.databinding.ActivityRegisterBinding
import br.com.fiap.challenge.databinding.LoginActivityBinding
import br.com.fiap.challenge.models.RiskStatus
import br.com.fiap.challenge.network.Dentist
import br.com.fiap.challenge.network.DentistRequestDTO
import br.com.fiap.challenge.network.createDentistService
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.registerToLoginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener {
            createDentist()
        }

    }

    private fun createDentist() = lifecycleScope.launch {
        try {

            val dentistName = binding.registerNameEditText.text.toString()
            val dentistSpecialty = binding.registerSpecialtyEditText.text.toString()
            val dentistRegistrationNumber = binding.registerRegistrationNumberEditText.text.toString()
            val dentistPassword = binding.registerPasswordEditText.text.toString()

            val dentistObject = DentistRequestDTO(
                dentistName,
                dentistSpecialty,
                dentistRegistrationNumber,
                dentistPassword,
                0.7,
                RiskStatus.ALTO
            )

            val dentistService = createDentistService()
            val response = dentistService.createDentist(dentistObject)

            if (response.isSuccessful) {

                Toast.makeText(
                    this@RegisterActivity,
                    "Cadastro efetuado! Faça o login no seu perfil!",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@RegisterActivity, "Erro", Toast.LENGTH_LONG).show()
            }

        } catch (ex: Exception) {

            Toast.makeText(
                this@RegisterActivity,
                ex.message,
                Toast.LENGTH_LONG
            ).show()
        }


    }
}
package br.com.fiap.challenge.ui.application

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import br.com.fiap.challenge.R
import br.com.fiap.challenge.databinding.ActivityConsultationDetailsBinding
import br.com.fiap.challenge.network.createConsultationService
import kotlinx.coroutines.launch

class ConsultationDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsultationDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConsultationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.detailsCardView.isVisible = false
        binding.patientNameTextView.isVisible = false
        binding.progressBar3.isVisible = true

        val consultaionId = intent.extras!!.getString("CONSULTATION_ID")!!

        lifecycleScope.launch {
            getConsultation(consultaionId)
        }


    }

    private suspend fun getConsultation(consultaionId: String) {

        try {

            val consultatinService = createConsultationService()
            val response = consultatinService.getConsultationById(consultaionId)

            if (response.isSuccessful){
                Toast.makeText(this, "Consulta carregada com sucesso!", Toast.LENGTH_LONG).show()
                val consultation = response.body()!!
                binding.patientNameTextView.text = consultation.patient.name
                binding.patientConsultatioDateTextView.text = consultation.consultationDate
                binding.patientConsultationFrequencyTextView.text = consultation.patient.consultationFrequency.toString()
                binding.patientGenderTextView.text = consultation.patient.gender
                binding.patientConsultationValueTextView.text = "R$ ${consultation.consultationValue.toString()}"
                binding.patientBirthdayTextView.text = consultation.patient.birthday
                binding.patientRiskStatusTextView.text = consultation.riskStatus.toString()
                val patientDescription = consultation.description

                if (patientDescription.isNullOrBlank()){
                    binding.consultationDescriptionTextView.isVisible = false
                    binding.patientDescriptionTextView.isVisible = false
                } else {
                    binding.patientDescriptionTextView.text = consultation.description
                }

                binding.detailsCardView.isVisible = true
                binding.patientNameTextView.isVisible = true
                binding.progressBar3.isVisible = false


            } else {
                Toast.makeText(
                    this,
                    "Não foi possível carregar a consulta",
                    Toast.LENGTH_LONG
                ).show()
            }

        } catch (ex: Exception) {
            Toast.makeText(
                this,
                ex.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
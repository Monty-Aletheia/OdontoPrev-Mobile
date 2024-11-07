package br.com.fiap.challenge.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import br.com.fiap.challenge.data.SessionManager
import br.com.fiap.challenge.databinding.FragmentProfileBinding
import br.com.fiap.challenge.network.ConsultationResponseDTO
import br.com.fiap.challenge.network.ConsultationService
import br.com.fiap.challenge.network.LoginDTO
import br.com.fiap.challenge.network.createAuthService
import br.com.fiap.challenge.network.createConsultationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val sessionToken = SessionManager.getSessionToken(requireContext())
            binding.testeView.text = sessionToken

            getConsultationsByDentist(sessionToken)


        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private suspend fun getConsultationsByDentist(dentistId: String): List<ConsultationResponseDTO>? {
        try {
            val consultaionService = createConsultationService()
            val response = consultaionService.getAllConsultations()

            if (response.isSuccessful) {
                val consultations = response.body()?._embedded?.consultationResponseDTOList
                val filteredConsultations = consultations?.filter { consultation ->
                    consultation.dentists.any { dentist -> dentist.id == dentistId }
                }
                return filteredConsultations

            } else {
                Log.e("API_ERROR", "Failed to fetch consultations")
                return null
            }
        } catch (ex: Exception) {

            Toast.makeText(
                requireContext(),
                ex.message,
                Toast.LENGTH_LONG
            ).show()
            return null
        }


    }
}

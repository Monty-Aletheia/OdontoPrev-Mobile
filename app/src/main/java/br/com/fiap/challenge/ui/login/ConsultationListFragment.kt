package br.com.fiap.challenge.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.challenge.R
import br.com.fiap.challenge.adapter.ConsultationAdapter
import br.com.fiap.challenge.data.SessionManager
import br.com.fiap.challenge.databinding.FragmentConsultationListBinding
import br.com.fiap.challenge.network.ConsultationResponseDTO
import br.com.fiap.challenge.network.createConsultationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConsultationListFragment : Fragment() {
    private var _binding: FragmentConsultationListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConsultationListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAllConsultations()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadAllConsultations() = lifecycleScope.launch(Dispatchers.Main) {
        val sessionToken = SessionManager.getSessionToken(requireContext())
        val list = getConsultationsByDentist(sessionToken)
        val recyclerView = binding.recyclerViewConsultationItem
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ConsultationAdapter(list)

    }



    private suspend fun getConsultationsByDentist(dentistId: String): List<ConsultationResponseDTO>{
        try {
            val consultaionService = createConsultationService()
            val response = consultaionService.getAllConsultations()

            if (response.isSuccessful) {
                val consultations = response.body()?._embedded?.consultationResponseDTOList
                val filteredConsultations = consultations!!.filter { consultation ->
                    consultation.dentists.any { dentist -> dentist.id == dentistId }
                }
                return filteredConsultations

            } else {
                Log.e("API_ERROR", "Failed to fetch consultations")
                return emptyList()
            }
        } catch (ex: Exception) {

            Toast.makeText(
                requireContext(),
                ex.message,
                Toast.LENGTH_LONG
            ).show()
            return emptyList()
        }


    }

}
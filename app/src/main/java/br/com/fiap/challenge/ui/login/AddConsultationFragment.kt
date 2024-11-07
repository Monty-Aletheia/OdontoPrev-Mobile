package br.com.fiap.challenge.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.challenge.R
import br.com.fiap.challenge.adapter.ConsultationAdapter
import br.com.fiap.challenge.data.SessionManager
import br.com.fiap.challenge.databinding.FragmentAddConsultationBinding
import br.com.fiap.challenge.databinding.FragmentProfileBinding
import br.com.fiap.challenge.network.ConsultationResponseDTO
import br.com.fiap.challenge.network.Patient
import br.com.fiap.challenge.network.createConsultationService
import br.com.fiap.challenge.network.createPatientService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddConsultationFragment : Fragment() {

    private var _binding: FragmentAddConsultationBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddConsultationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val list = getAllPatients()
            val patientNames = list.map { it.name }

            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, patientNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.spinner.adapter = adapter
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedPatientName = parent.getItemAtPosition(position) as String
                    Toast.makeText(
                        requireContext(),
                        "Paciente selecionado: $selectedPatientName",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun getAllPatients(): List<Patient> {
        try {
            val patientService = createPatientService()
            val response = patientService.getAllPatients()

            if (response.isSuccessful) {
                val patients = response.body()!!._embedded.patientResponseDTOList
                return patients
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
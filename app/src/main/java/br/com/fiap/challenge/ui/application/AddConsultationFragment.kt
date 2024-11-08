package br.com.fiap.challenge.ui.application

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import br.com.fiap.challenge.data.SessionManager
import br.com.fiap.challenge.databinding.FragmentAddConsultationBinding
import br.com.fiap.challenge.models.RiskStatus
import br.com.fiap.challenge.network.ConsultationRequestDTO
import br.com.fiap.challenge.network.Patient
import br.com.fiap.challenge.network.createConsultationService
import br.com.fiap.challenge.network.createPatientService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

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

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val dateEditText = binding.dateEditText

        val dateTime: String? = null

        dateEditText.setOnClickListener {
            openDatePickerDialog(dateFormat, dateEditText, dateTime)
        }

        var selectedPatientId = "@Null"

        binding.createButton.setOnClickListener {
            createConsultation(selectedPatientId)
        }

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
                    selectedPatientId = list[position].id

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


    private fun openDatePickerDialog(
        dateFormat: SimpleDateFormat,
        dateEditText: EditText,
        dateTime: String?
    ) {
        var dateTime1 = dateTime
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val selectedDate = dateFormat.format(calendar.time)
                dateEditText.setText(selectedDate)

                dateTime1 = selectedDate
            },
            year, month, day
        )
        datePickerDialog.show()
    }


    private fun createConsultation(selectedPatienteId: String) = lifecycleScope.launch {

        try {


            val consultationDate = binding.dateEditText.text.toString()
            val consultationValue = binding.consultationValueEditText.text.toString().toDouble()
            val consultationDescription = binding.descriptionEditText.text.toString()
            val sessionToken = SessionManager.getSessionToken(requireContext())
            val dentistId = mutableListOf<String>()
            dentistId.add(sessionToken)

            val consultationRequestDTO = ConsultationRequestDTO(
                consultationDate,
                consultationValue,
                RiskStatus.ALTO,
                selectedPatienteId,
                dentistId,
                consultationDescription
            )
            val consultationService = createConsultationService()
            val response = consultationService.createNewConsultation(consultationRequestDTO)

            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "Consulta criada com sucesso", Toast.LENGTH_LONG)
                    .show()

            } else {
                Toast.makeText(requireContext(), "Erro: ${response.message()}", Toast.LENGTH_LONG)
                    .show()
                println(consultationRequestDTO)
            }


        } catch (ex: Exception) {

            Toast.makeText(
                requireContext(),
                ex.message,
                Toast.LENGTH_LONG
            ).show()
        }
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
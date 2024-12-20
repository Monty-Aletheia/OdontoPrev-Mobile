package br.com.fiap.challenge.ui.application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import br.com.fiap.challenge.data.SessionManager
import br.com.fiap.challenge.databinding.FragmentProfileBinding
import br.com.fiap.challenge.network.createDentistService
import kotlinx.coroutines.launch


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

        binding.dentistProfileNameTextView.isVisible = false
        binding.dentistProfileRegisterNumberTextView.isVisible = false
        binding.dentistSpecialtyTextView.isVisible = false
        binding.specialtyTextView.isVisible = false
        binding.profileRegisterNumberTextView.isVisible = false
        binding.nameProgressBar.isVisible = true
        binding.profileInfosProgessBar.isVisible = true

        lifecycleScope.launch {
            getDentistProfile()
        }


    }

    private suspend fun getDentistProfile(){

        try {

            val dentistId = SessionManager.getSessionToken(requireContext())
            val dentistService = createDentistService()
            val response = dentistService.getDentistById(dentistId)

            if (response.isSuccessful){
                val dentist = response.body()!!

                binding.dentistProfileNameTextView.text = dentist.name
                binding.dentistProfileRegisterNumberTextView.text = dentist.registrationNumber
                binding.dentistSpecialtyTextView.text = dentist.specialty

                binding.dentistProfileNameTextView.isVisible = true
                binding.dentistProfileRegisterNumberTextView.isVisible = true
                binding.dentistSpecialtyTextView.isVisible = true
                binding.specialtyTextView.isVisible = true
                binding.profileRegisterNumberTextView.isVisible = true
                binding.nameProgressBar.isVisible = false
                binding.profileInfosProgessBar.isVisible = false
            } else {
                Toast.makeText(requireContext(), "Não foi possível econtrar suas informações", Toast.LENGTH_LONG).show()
            }

        } catch (ex: Exception) {
            Toast.makeText(
                requireContext(),
                ex.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}

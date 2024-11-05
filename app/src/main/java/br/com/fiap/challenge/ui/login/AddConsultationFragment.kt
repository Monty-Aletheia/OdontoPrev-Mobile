package br.com.fiap.challenge.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.fiap.challenge.R
import br.com.fiap.challenge.databinding.FragmentAddConsultationBinding
import br.com.fiap.challenge.databinding.FragmentProfileBinding

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
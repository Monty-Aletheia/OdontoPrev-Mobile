package br.com.fiap.challenge.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.fiap.challenge.R
import br.com.fiap.challenge.adapter.ConsultationAdapter
import br.com.fiap.challenge.databinding.FragmentConsultationListBinding

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

        binding.recyclerViewConsultationItem.adapter = ConsultationAdapter()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
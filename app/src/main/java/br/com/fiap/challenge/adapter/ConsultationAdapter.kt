package br.com.fiap.challenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.challenge.databinding.ConsultationItemBinding
import br.com.fiap.challenge.network.ConsultationResponse
import br.com.fiap.challenge.network.ConsultationResponseDTO

class ConsultationAdapter(val listConsultations: List<ConsultationResponseDTO>) : RecyclerView.Adapter<ConsultationAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ConsultationItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(consultation: ConsultationResponseDTO){
            binding.userName.text = consultation.patient.name
            binding.userDate.text = consultation.consultationDate
            binding.userValue.text = consultation.consultationValue.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultationAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ConsultationItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listConsultations[position]

        holder.bind(item)
    }

    override fun getItemCount() = listConsultations.size


}

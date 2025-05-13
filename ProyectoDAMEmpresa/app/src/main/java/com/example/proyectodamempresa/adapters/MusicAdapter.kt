package com.example.proyectodamempresa.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodamempresa.databinding.ItemMixBinding
import com.example.proyectodamempresa.models.Track

class MusicAdapter(private val lista: List<Track>) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    inner class MusicViewHolder(val binding: ItemMixBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = ItemMixBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val track = lista[position]
        holder.binding.textoTitulo.text = track.name
        holder.binding.textoArtista.text = track.artist

        // Puedes añadir un evento si quieres que al pulsar se abra un fragment o algo
        holder.itemView.setOnClickListener {
            // Aquí se puede añadir lógica para reproducir o abrir el detalle
        }
    }

    override fun getItemCount(): Int = lista.size
}
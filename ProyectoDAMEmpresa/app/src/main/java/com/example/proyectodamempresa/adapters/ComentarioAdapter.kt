package com.example.proyectodamempresa.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodamempresa.databinding.ItemComentarioBinding
import com.example.proyectodamempresa.models.Comentario

class ComentarioAdapter(private val listaComentarios: List<Comentario>) :
    RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder>() {

    inner class ComentarioViewHolder(val binding: ItemComentarioBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentarioViewHolder {
        val binding = ItemComentarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComentarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComentarioViewHolder, position: Int) {
        val comentario = listaComentarios[position]
        holder.binding.usuarioComentario.text = comentario.id_usuario ?: "Anónimo"
        holder.binding.textoComentario.text = comentario.texto
        holder.binding.fechaComentario.text = comentario.fecha
    }

    override fun getItemCount(): Int = listaComentarios.size
}
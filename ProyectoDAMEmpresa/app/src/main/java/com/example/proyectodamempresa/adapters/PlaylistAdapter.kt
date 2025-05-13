package com.example.proyectodamempresa.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodamempresa.VerPlaylistActivity
import com.example.proyectodamempresa.databinding.ItemPlaylistBinding
import com.example.proyectodamempresa.models.Playlist

class PlaylistAdapter(
    private val lista: List<Playlist>,
    private val onClick: ((Playlist) -> Unit)? = null
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = lista[position]
        holder.binding.textoNombrePlaylist.text = playlist.nombre
        holder.binding.textoVisibilidad.text = if (playlist.publica == true) "Pública" else "Privada"
        holder.binding.textoCreador.text = "Por: ${playlist.correoCreador ?: "desconocido"}"

        holder.itemView.setOnClickListener {
            onClick?.invoke(playlist)
        }
    }

    override fun getItemCount(): Int = lista.size
}
package com.example.proyectodamempresa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodamempresa.databinding.ItemCancionBinding
import com.example.proyectodamempresa.models.Track
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CancionAdapter(
    private val canciones: List<Track>,
    private val modoFavoritos: Boolean = false,
    private val modoBusqueda: Boolean = false,
    private val playlistIdParaEliminar: String? = null
) : RecyclerView.Adapter<CancionAdapter.CancionViewHolder>() {

    inner class CancionViewHolder(val binding: ItemCancionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancionViewHolder {
        val binding = ItemCancionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CancionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CancionViewHolder, position: Int) {
        val cancion = canciones[position]
        holder.binding.textoNombre.text = cancion.name
        holder.binding.textoArtista.text = "Artista: ${cancion.artist}"


        holder.binding.botonFavorito.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val referencia = FirebaseDatabase.getInstance().getReference("usuarios").child(uid!!)
                .child("favoritos")
            val idCancion = "${cancion.name}_${cancion.artist}".replace(".", "")
            referencia.child(idCancion).setValue(cancion)
        }

        if (modoFavoritos || modoBusqueda) {
            holder.binding.botonAgregarAPlaylist.visibility = View.VISIBLE

            holder.binding.botonAgregarAPlaylist.setOnClickListener {
                mostrarPlaylists(holder.itemView.context, cancion)
            }
        } else {
            holder.binding.botonAgregarAPlaylist.visibility = View.GONE
        }
        if (playlistIdParaEliminar != null) {
            holder.binding.botonEliminar.visibility = View.VISIBLE

            holder.binding.botonEliminar.setOnClickListener {
                val idCancion = "${cancion.name}_${cancion.artist}".replace(".", "")
                val ref = FirebaseDatabase.getInstance()
                    .getReference("playlists")
                    .child(playlistIdParaEliminar)
                    .child("canciones")
                    .child(idCancion)

                ref.removeValue().addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "Canción eliminada", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            holder.binding.botonEliminar.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = canciones.size

    private fun mostrarPlaylists(context: Context, cancion: Track) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("playlists")

        ref.orderByChild("creador").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = ArrayList<String>()
                    val ids = ArrayList<String>()

                    for (playlist in snapshot.children) {
                        val nombre = playlist.child("nombre").value.toString()
                        val id = playlist.key ?: continue
                        nombres.add(nombre)
                        ids.add(id)
                    }

                    if (nombres.isEmpty()) {
                        Toast.makeText(context, "No tienes playlists", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Elige una playlist")
                    builder.setItems(nombres.toTypedArray()) { _, i ->
                        val playlistId = ids[i]
                        val idCancion = "${cancion.name}_${cancion.artist}".replace(".", "")
                        FirebaseDatabase.getInstance()
                            .getReference("playlists")
                            .child(playlistId)
                            .child("canciones")
                            .child(idCancion)
                            .setValue(cancion)

                        Toast.makeText(context, "Canción añadida", Toast.LENGTH_SHORT).show()
                    }
                    builder.show()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
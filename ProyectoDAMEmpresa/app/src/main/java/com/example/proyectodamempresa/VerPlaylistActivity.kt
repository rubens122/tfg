package com.example.proyectodamempresa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodamempresa.adapters.CancionAdapter
import com.example.proyectodamempresa.adapters.PlaylistAdapter
import com.example.proyectodamempresa.databinding.ActivityVerPlaylistBinding
import com.example.proyectodamempresa.models.Track
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VerPlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerPlaylistBinding
    private lateinit var database: DatabaseReference
    private lateinit var adapter: CancionAdapter
    private val listaCanciones = mutableListOf<Track>()

    private lateinit var playlistId: String
    private lateinit var nombrePlaylist: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playlistId = intent.getStringExtra("playlistId") ?: ""
        nombrePlaylist = intent.getStringExtra("nombre") ?: ""

        binding.textoNombrePlaylist.text = nombrePlaylist
        binding.recyclerCanciones.layoutManager = LinearLayoutManager(this)
        database = FirebaseDatabase.getInstance()
            .getReference("playlists").child(playlistId).child("canciones")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaCanciones.clear()
                for (item in snapshot.children) {
                    val cancion = item.getValue(Track::class.java)
                    if (cancion != null) listaCanciones.add(cancion)
                }
                adapter = CancionAdapter(listaCanciones, playlistIdParaEliminar = playlistId)
                binding.recyclerCanciones.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@VerPlaylistActivity, "Error al cargar canciones", Toast.LENGTH_SHORT).show()
                Log.e("VerPlaylist", error.message)
            }
        })

        // Botón editar
        binding.botonEditar.setOnClickListener {
            val intent = Intent(this, EditarPlaylistActivity::class.java)
            intent.putExtra("playlistId", playlistId)
            intent.putExtra("nombre", nombrePlaylist)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar nombre actualizado al volver de EditarPlaylistActivity
        FirebaseDatabase.getInstance().getReference("playlists").child(playlistId)
            .child("nombre").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombreActualizado = snapshot.getValue(String::class.java)
                    if (!nombreActualizado.isNullOrEmpty()) {
                        binding.textoNombrePlaylist.text = nombreActualizado
                        nombrePlaylist = nombreActualizado
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
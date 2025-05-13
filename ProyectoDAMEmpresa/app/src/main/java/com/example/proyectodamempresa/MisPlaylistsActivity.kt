package com.example.proyectodamempresa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodamempresa.adapters.PlaylistAdapter
import com.example.proyectodamempresa.databinding.ActivityMisPlaylistsBinding
import com.example.proyectodamempresa.models.Playlist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MisPlaylistsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMisPlaylistsBinding
    private lateinit var adapter: PlaylistAdapter
    private val lista = mutableListOf<Playlist>()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisPlaylistsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerPlaylists.layoutManager = LinearLayoutManager(this)

        adapter = PlaylistAdapter(lista) { playlist ->
            val intent = Intent(this@MisPlaylistsActivity, VerPlaylistActivity::class.java)
            intent.putExtra("playlistId", playlist.id)
            intent.putExtra("nombre", playlist.nombre)
            startActivity(intent)
        }

        binding.recyclerPlaylists.adapter = adapter
        cargarPlaylists()
    }

    override fun onResume() {
        super.onResume()
        cargarPlaylists()
    }

    private fun cargarPlaylists() {
        if (uid == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("playlists")
        val usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios").child(uid)

        usuarioRef.child("correo").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(correoSnapshot: DataSnapshot) {
                val correo = correoSnapshot.value?.toString() ?: "desconocido"

                ref.orderByChild("creador").equalTo(uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            lista.clear()
                            for (playlistSnap in snapshot.children) {
                                val playlist = playlistSnap.getValue(Playlist::class.java)
                                playlist?.id = playlistSnap.key
                                playlist?.correoCreador = correo
                                playlist?.let { lista.add(it) }
                            }
                            adapter.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@MisPlaylistsActivity, "Error al cargar playlists", Toast.LENGTH_SHORT).show()
                            Log.e("MisPlaylists", error.message)
                        }
                    })
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MisPlaylistsActivity, "Error al cargar correo", Toast.LENGTH_SHORT).show()
                Log.e("CorreoUsuario", error.message)
            }
        })
    }
}
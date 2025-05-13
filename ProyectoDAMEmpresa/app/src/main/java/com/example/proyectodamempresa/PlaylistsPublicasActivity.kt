package com.example.proyectodamempresa

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodamempresa.adapters.PlaylistAdapter
import com.example.proyectodamempresa.databinding.ActivityPlaylistsPublicasBinding
import com.example.proyectodamempresa.models.Playlist
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PlaylistsPublicasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistsPublicasBinding
    private lateinit var adapter: PlaylistAdapter
    private val lista = mutableListOf<Playlist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistsPublicasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerPlaylistsPublicas.layoutManager = LinearLayoutManager(this)

        cargarPlaylists()

        binding.campoBusqueda.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString().lowercase()
                val filtradas = lista.filter {
                    it.nombre?.lowercase()?.contains(texto) == true ||
                            it.correoCreador?.lowercase()?.contains(texto) == true
                }
                adapter = PlaylistAdapter(filtradas) { playlist ->
                    val intent = Intent(this@PlaylistsPublicasActivity, VerPlaylistActivity::class.java)
                    intent.putExtra("playlistId", playlist.id)
                    intent.putExtra("nombre", playlist.nombre)
                    startActivity(intent)
                }
                binding.recyclerPlaylistsPublicas.adapter = adapter
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun cargarPlaylists() {
        val ref = FirebaseDatabase.getInstance().getReference("playlists")
        ref.orderByChild("publica").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    lista.clear()
                    var total = snapshot.childrenCount
                    var procesadas = 0L

                    for (playlistSnap in snapshot.children) {
                        val playlist = playlistSnap.getValue(Playlist::class.java)
                        playlist?.id = playlistSnap.key

                        val uid = playlist?.creador
                        if (uid != null && playlist != null) {
                            val usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios")
                            usuarioRef.child(uid).child("correo")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(data: DataSnapshot) {
                                        playlist.correoCreador = data.value?.toString()
                                        lista.add(playlist)
                                        procesadas++
                                        if (procesadas == total) {
                                            mostrarLista()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        procesadas++
                                        if (procesadas == total) {
                                            mostrarLista()
                                        }
                                    }
                                })
                        } else {
                            procesadas++
                            if (procesadas == total) {
                                mostrarLista()
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@PlaylistsPublicasActivity, "Error al cargar", Toast.LENGTH_SHORT).show()
                    Log.e("Publicas", error.message)
                }
            })
    }

    private fun mostrarLista() {
        adapter = PlaylistAdapter(lista) { playlist ->
            val intent = Intent(this, VerPlaylistActivity::class.java)
            intent.putExtra("playlistId", playlist.id)
            intent.putExtra("nombre", playlist.nombre)
            startActivity(intent)
        }
        binding.recyclerPlaylistsPublicas.adapter = adapter
    }
}
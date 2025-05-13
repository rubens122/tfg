package com.example.proyectodamempresa

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectodamempresa.databinding.ActivityCrearPlaylistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class CrearPlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearPlaylistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.botonCrearPlaylist.setOnClickListener {
            val nombre = binding.campoNombrePlaylist.text.toString().trim()
            val esPublica = binding.checkPublica.isChecked
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Introduce un nombre para la playlist", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = UUID.randomUUID().toString()
            val datos = mapOf(
                "nombre" to nombre,
                "publica" to esPublica,
                "creador" to uid
            )

            FirebaseDatabase.getInstance().getReference("playlists")
                .child(id)
                .setValue(datos)
                .addOnSuccessListener {
                    Toast.makeText(this, "Playlist creada con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al crear la playlist", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
package com.example.proyectodamempresa

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectodamempresa.databinding.ActivityEditarPlaylistBinding
import com.google.firebase.database.FirebaseDatabase

class EditarPlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPlaylistBinding
    private var playlistId: String? = null
    private var playlistNombre: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playlistId = intent.getStringExtra("playlistId")
        playlistNombre = intent.getStringExtra("nombre")

        if (playlistId == null) {
            Toast.makeText(this, "Error: ID no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.campoNombre.setText(playlistNombre)

        binding.botonGuardar.setOnClickListener {
            val nuevoNombre = binding.campoNombre.text.toString().trim()
            val esPublica = binding.checkPublica.isChecked

            if (nuevoNombre.isEmpty()) {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ref = FirebaseDatabase.getInstance().getReference("playlists").child(playlistId!!)
            val actualizaciones = mapOf(
                "nombre" to nuevoNombre,
                "publica" to esPublica
            )

            ref.updateChildren(actualizaciones).addOnSuccessListener {
                Toast.makeText(this, "Playlist actualizada", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }

        binding.botonEliminar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("¿Eliminar playlist?")
                .setMessage("Esta acción eliminará la playlist y todas sus canciones.")
                .setPositiveButton("Eliminar") { _: DialogInterface, _: Int ->
                    eliminarPlaylist()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun eliminarPlaylist() {
        val ref = FirebaseDatabase.getInstance().getReference("playlists").child(playlistId!!)
        ref.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Playlist eliminada", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
        }
    }
}
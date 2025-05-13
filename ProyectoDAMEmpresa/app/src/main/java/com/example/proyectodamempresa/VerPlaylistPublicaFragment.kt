package com.example.proyectodamempresa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodamempresa.adapters.CancionAdapter
import com.example.proyectodamempresa.adapters.ComentarioAdapter
import com.example.proyectodamempresa.databinding.FragmentVerPlaylistPublicaBinding
import com.example.proyectodamempresa.models.Comentario
import com.example.proyectodamempresa.models.Track
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VerPlaylistPublicaFragment : Fragment() {

    private var _binding: FragmentVerPlaylistPublicaBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var adapterCanciones: CancionAdapter
    private lateinit var adapterComentarios: ComentarioAdapter
    private val listaCanciones = mutableListOf<Track>()
    private val listaComentarios = mutableListOf<Comentario>()
    private var playlistId: String? = null

    companion object {
        fun newInstance(playlistId: String): VerPlaylistPublicaFragment {
            val fragment = VerPlaylistPublicaFragment()
            val args = Bundle()
            args.putString("playlistId", playlistId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerPlaylistPublicaBinding.inflate(inflater, container, false)
        playlistId = arguments?.getString("playlistId")

        binding.recyclerCanciones.layoutManager = LinearLayoutManager(context)
        binding.recyclerComentarios.layoutManager = LinearLayoutManager(context)

        // 🔄 Cargar canciones y comentarios
        cargarCanciones()
        cargarComentarios()

        // ➕ Añadir comentario
        binding.botonEnviarComentario.setOnClickListener {
            enviarComentario()
        }

        return binding.root
    }

    private fun cargarCanciones() {
        playlistId?.let {
            database = FirebaseDatabase.getInstance()
                .getReference("playlists")
                .child(it)
                .child("canciones")

            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listaCanciones.clear()
                    for (item in snapshot.children) {
                        val cancion = item.getValue(Track::class.java)
                        if (cancion != null) listaCanciones.add(cancion)
                    }
                    adapterCanciones = CancionAdapter(listaCanciones, playlistIdParaEliminar = it)
                    binding.recyclerCanciones.adapter = adapterCanciones
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error al cargar canciones", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun cargarComentarios() {
        playlistId?.let {
            database = FirebaseDatabase.getInstance()
                .getReference("playlists")
                .child(it)
                .child("comentarios")

            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listaComentarios.clear()
                    for (item in snapshot.children) {
                        val comentario = item.getValue(Comentario::class.java)
                        if (comentario != null) listaComentarios.add(comentario)
                    }
                    adapterComentarios = ComentarioAdapter(listaComentarios)
                    binding.recyclerComentarios.adapter = adapterComentarios
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error al cargar comentarios", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun enviarComentario() {
        val textoComentario = binding.campoComentario.text.toString().trim()
        if (textoComentario.isNotEmpty()) {
            val nuevoComentario = Comentario(
                id_usuario = FirebaseAuth.getInstance().currentUser?.email,
                texto = textoComentario,
                fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            )
            database.push().setValue(nuevoComentario).addOnSuccessListener {
                binding.campoComentario.text.clear()
                Toast.makeText(context, "Comentario añadido", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Error al añadir comentario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
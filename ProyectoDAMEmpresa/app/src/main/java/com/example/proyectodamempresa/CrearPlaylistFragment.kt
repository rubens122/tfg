package com.example.proyectodamempresa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectodamempresa.databinding.FragmentCrearPlaylistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CrearPlaylistFragment : Fragment() {

    private var _binding: FragmentCrearPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearPlaylistBinding.inflate(inflater, container, false)

        binding.botonGuardarPlaylist.setOnClickListener {
            val nombre = binding.campoNombrePlaylist.text.toString()
            val publica = binding.switchPublica.isChecked
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            if (uid != null && nombre.isNotEmpty()) {
                val ref = FirebaseDatabase.getInstance().getReference("playlists").push()
                val nuevaPlaylist = mapOf(
                    "nombre" to nombre,
                    "publica" to publica,
                    "creador" to uid
                )
                ref.setValue(nuevaPlaylist)
                parentFragmentManager.popBackStack() // Volver atrás
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
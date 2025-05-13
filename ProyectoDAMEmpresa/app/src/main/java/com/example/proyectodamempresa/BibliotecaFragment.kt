package com.example.proyectodamempresa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectodamempresa.databinding.FragmentBibliotecaBinding

class BibliotecaFragment : Fragment() {

    private var _binding: FragmentBibliotecaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBibliotecaBinding.inflate(inflater, container, false)

        // Listener para "Mis Playlists"
        binding.opcionMisListas.setOnClickListener {
            cargarFragment(MisPlaylistsFragment())
        }

        // Listener para "Canciones Favoritas"
        binding.opcionFavoritas.setOnClickListener {
            cargarFragment(FavoritosFragment())
        }

        // Listener para crear una nueva playlist
        binding.botonCrearPlaylist.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, CrearPlaylistFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    // Método para cargar el fragmento y ocupar toda la pantalla del contenedor
    private fun cargarFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
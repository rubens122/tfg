package com.example.proyectodamempresa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodamempresa.adapters.CancionAdapter
import com.example.proyectodamempresa.adapters.PlaylistAdapter
import com.example.proyectodamempresa.databinding.FragmentMisPlaylistsBinding
import com.example.proyectodamempresa.models.Playlist
import com.example.proyectodamempresa.models.Track
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MisPlaylistsFragment : Fragment() {

    private var _binding: FragmentMisPlaylistsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlaylistAdapter
    private val listaPlaylists = mutableListOf<Playlist>()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMisPlaylistsBinding.inflate(inflater, container, false)

        binding.recyclerPlaylists.layoutManager = LinearLayoutManager(context)
        cargarMisPlaylists()

        return binding.root
    }

    private fun cargarMisPlaylists() {
        val ref = FirebaseDatabase.getInstance().getReference("playlists")
        ref.orderByChild("creador").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listaPlaylists.clear()
                    for (playlistSnap in snapshot.children) {
                        val playlist = playlistSnap.getValue(Playlist::class.java)
                        playlist?.id = playlistSnap.key
                        if (playlist != null) {
                            listaPlaylists.add(playlist)
                        }
                    }
                    adapter = PlaylistAdapter(listaPlaylists) { playlist ->
                        abrirVerPlaylistFragment(playlist.id ?: "")
                    }
                    binding.recyclerPlaylists.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error al cargar playlists", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun abrirVerPlaylistFragment(playlistId: String) {
        val fragment = VerPlaylistFragment.newInstance(playlistId)
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

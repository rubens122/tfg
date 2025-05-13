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
import com.example.proyectodamempresa.databinding.FragmentPlaylistsPublicasBinding
import com.example.proyectodamempresa.models.Playlist
import com.example.proyectodamempresa.models.Track
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PlaylistsPublicasFragment : Fragment() {

    private var _binding: FragmentPlaylistsPublicasBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var adapter: PlaylistAdapter
    private val listaPlaylists = mutableListOf<Playlist>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsPublicasBinding.inflate(inflater, container, false)

        binding.recyclerPlaylistsPublicas.layoutManager = LinearLayoutManager(context)

        database = FirebaseDatabase.getInstance().getReference("playlists")
        database.orderByChild("publica").equalTo(true)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!isAdded || _binding == null) return  // 🔴 Verifica si el fragment sigue montado

                    listaPlaylists.clear()
                    for (item in snapshot.children) {
                        val playlist = item.getValue(Playlist::class.java)
                        playlist?.id = item.key
                        if (playlist != null) {
                            listaPlaylists.add(playlist)
                        }
                    }
                    adapter = PlaylistAdapter(listaPlaylists) { playlist ->
                        val fragment = VerPlaylistFragment.newInstance(playlist.id!!)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.contenedorFragment, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    binding.recyclerPlaylistsPublicas.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    if (!isAdded) return
                    Toast.makeText(context, "Error al cargar playlists", Toast.LENGTH_SHORT).show()
                }
            })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
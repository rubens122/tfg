package com.example.proyectodamempresa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodamempresa.adapters.CancionAdapter
import com.example.proyectodamempresa.databinding.FragmentFavoritosBinding
import com.example.proyectodamempresa.models.Track
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoritosFragment : Fragment() {

    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CancionAdapter
    private val listaCanciones = mutableListOf<Track>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        binding.recyclerFavoritos.layoutManager = LinearLayoutManager(context)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().getReference("usuarios").child(uid!!).child("favoritos")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaCanciones.clear()
                for (item in snapshot.children) {
                    val cancion = item.getValue(Track::class.java)
                    if (cancion != null) listaCanciones.add(cancion)
                }
                adapter = CancionAdapter(listaCanciones)
                binding.recyclerFavoritos.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
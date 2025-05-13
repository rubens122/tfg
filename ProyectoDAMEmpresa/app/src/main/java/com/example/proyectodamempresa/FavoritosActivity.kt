package com.example.proyectodamempresa

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodamempresa.adapters.CancionAdapter
import com.example.proyectodamempresa.databinding.ActivityFavoritosBinding
import com.example.proyectodamempresa.models.Track
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoritosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritosBinding
    private lateinit var database: DatabaseReference
    private lateinit var adapter: CancionAdapter
    private val listaFavoritos = mutableListOf<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerFavoritos.layoutManager = LinearLayoutManager(this)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            database = FirebaseDatabase.getInstance().getReference("usuarios").child(uid).child("favoritos")

            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listaFavoritos.clear()
                    for (hijo in snapshot.children) {
                        val cancion = hijo.getValue(Track::class.java)
                        cancion?.let { listaFavoritos.add(it) }
                    }
                    adapter = CancionAdapter(listaFavoritos, modoFavoritos = true)
                    binding.recyclerFavoritos.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@FavoritosActivity, "Error al cargar favoritos", Toast.LENGTH_SHORT).show()
                    Log.e("FavoritosActivity", error.message)
                }
            })
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}
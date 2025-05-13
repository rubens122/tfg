package com.example.proyectodamempresa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.proyectodamempresa.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar por defecto el HomeFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragment, HomeFragment())
            .commit()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_buscar -> BuscarFragment()
                R.id.nav_radio -> PlaylistsPublicasFragment()
                R.id.nav_biblioteca -> BibliotecaFragment() // 🔄 Aquí cambiamos
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, fragment)
                .commit()
            true
        }

    }
}
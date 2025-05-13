package com.example.proyectodamempresa

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodamempresa.adapters.CancionAdapter
import com.example.proyectodamempresa.api.RespuestaBusqueda
import com.example.proyectodamempresa.api.RetrofitClient
import com.example.proyectodamempresa.databinding.ActivityBuscarCancionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscarCancionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuscarCancionBinding
    private val apiKey = "91b36e37d60051ac5cfe4964d089d287"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarCancionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerCanciones.layoutManager = LinearLayoutManager(this)


        binding.botonBuscar.setOnClickListener {
            val nombre = binding.campoNombreCancion.text.toString().trim()
            if (nombre.isNotEmpty()) {
                buscarCancion(nombre)
            } else {
                Toast.makeText(this, "Escribe el nombre de una canción", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buscarCancion(nombre: String) {
        RetrofitClient.instance.buscarCanciones(nombre, apiKey)
            .enqueue(object : Callback<RespuestaBusqueda> {
                override fun onResponse(call: Call<RespuestaBusqueda>, response: Response<RespuestaBusqueda>) {
                    if (response.isSuccessful) {
                        val canciones = response.body()?.results?.trackmatches?.track
                        if (!canciones.isNullOrEmpty()) {
                            binding.recyclerCanciones.adapter = CancionAdapter(canciones, modoBusqueda = true)
                        } else {
                            Toast.makeText(this@BuscarCancionActivity, "No se encontraron canciones", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@BuscarCancionActivity, "Error al buscar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RespuestaBusqueda>, t: Throwable) {
                    Log.e("BuscarCancion", "Fallo: ${t.message}")
                    Toast.makeText(this@BuscarCancionActivity, "Fallo de red", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
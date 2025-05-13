package com.example.proyectodamempresa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodamempresa.adapters.CancionAdapter
import com.example.proyectodamempresa.api.RespuestaBusqueda
import com.example.proyectodamempresa.api.RetrofitClient
import com.example.proyectodamempresa.databinding.FragmentBuscarBinding
import com.example.proyectodamempresa.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BuscarFragment : Fragment() {

    private var _binding: FragmentBuscarBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CancionAdapter
    private val listaCanciones = mutableListOf<Track>()
    private val apiKey = "91b36e37d60051ac5cfe4964d089d287"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuscarBinding.inflate(inflater, container, false)

        // Configuración del RecyclerView
        binding.recyclerCanciones.layoutManager = LinearLayoutManager(context)
        adapter = CancionAdapter(listaCanciones, modoBusqueda = true)
        binding.recyclerCanciones.adapter = adapter

        // Lógica del botón de búsqueda
        binding.botonBuscar.setOnClickListener {
            val nombre = binding.campoBusqueda.text.toString().trim()
            if (nombre.isNotEmpty()) {
                buscarCancion(nombre)
            } else {
                Toast.makeText(context, "Escribe el nombre de una canción", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun buscarCancion(nombre: String) {
        RetrofitClient.instance.buscarCanciones(nombre, apiKey)
            .enqueue(object : Callback<RespuestaBusqueda> {
                override fun onResponse(call: Call<RespuestaBusqueda>, response: Response<RespuestaBusqueda>) {
                    if (response.isSuccessful) {
                        val canciones = response.body()?.results?.trackmatches?.track
                        if (!canciones.isNullOrEmpty()) {
                            listaCanciones.clear()
                            listaCanciones.addAll(canciones)
                            adapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(context, "No se encontraron canciones", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Error al buscar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RespuestaBusqueda>, t: Throwable) {
                    Log.e("BuscarFragment", "Fallo: ${t.message}")
                    Toast.makeText(context, "Fallo de red", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
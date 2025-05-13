package com.example.proyectodamempresa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodamempresa.adapters.CancionAdapter
import com.example.proyectodamempresa.databinding.FragmentHomeBinding
import com.example.proyectodamempresa.models.Track

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterMixes: MusicAdapter
    private lateinit var adapterSumergir: MusicAdapter
    private val listaMixes = mutableListOf<Track>()
    private val listaSumergir = mutableListOf<Track>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Configurar RecyclerView
        binding.recyclerMixes.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerSumergir.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Adaptadores
        adapterMixes = MusicAdapter(listaMixes)
        adapterSumergir = MusicAdapter(listaSumergir)

        binding.recyclerMixes.adapter = adapterMixes
        binding.recyclerSumergir.adapter = adapterSumergir

        // Cargar datos (Ejemplo)
        cargarMixes()
        cargarSumergir()

        return binding.root
    }

    private fun cargarMixes() {
        // Aquí se cargan los mixes, por ejemplo:
        listaMixes.add(Track("Mix latino", "Bad Bunny"))
        listaMixes.add(Track("Mix de Rels B", "Rels B"))
        listaMixes.add(Track("Mix diario 1", "Varios"))
        adapterMixes.notifyDataSetChanged()
    }

    private fun cargarSumergir() {
        // Aquí se cargan las recomendaciones:
        listaSumergir.add(Track("Ignorantes", "Bad Bunny"))
        listaSumergir.add(Track("Flakk Daniels", "Rels B"))
        listaSumergir.add(Track("Drama", "Celia"))
        adapterSumergir.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
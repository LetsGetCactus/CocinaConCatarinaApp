package local.pmdm.cocinaconcatarinaapp.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import local.pmdm.cocinaconcatarinaapp.R
import local.pmdm.cocinaconcatarinaapp.databinding.FragmentHomeBinding


class Home : Fragment() {
    private var _binding: FragmentHomeBinding?=null
    private val binding get()= checkNotNull(_binding){
        "La vista no ha sido inicializada"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.VISIBLE

        binding.BtAllRecetas.setOnClickListener{
            // Creamos la acción de navegación generada por Safe Args
            // Pasamos el argumento 'categoria' con el valor "All"
            val action = HomeDirections.actionHomeToListadoRecetas(categoria = "All")
            // Navegamos usando el NavController y la acción creada
            findNavController().navigate(action)
        }
        binding.BtDulces.setOnClickListener{
            val action = HomeDirections.actionHomeToListadoRecetas(categoria = "Dulce")
            findNavController().navigate(action)
        }
        binding.BtSaladas.setOnClickListener{
            val action = HomeDirections.actionHomeToListadoRecetas(categoria = "Salado")
            findNavController().navigate(action)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}
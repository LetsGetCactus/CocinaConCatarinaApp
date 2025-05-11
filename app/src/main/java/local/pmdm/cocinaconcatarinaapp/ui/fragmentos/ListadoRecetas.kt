package local.pmdm.cocinaconcatarinaapp.ui.fragmentos

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import local.pmdm.cocinaconcatarinaapp.R
import local.pmdm.cocinaconcatarinaapp.db.data.RecetasDataSource
import local.pmdm.cocinaconcatarinaapp.databinding.FragmentListadoRecetasBinding
import local.pmdm.cocinaconcatarinaapp.model.Receta
import local.pmdm.cocinaconcatarinaapp.ui.adapters.RecetaAdapter
import java.util.Locale


class ListadoRecetas : Fragment() {
    private var _binding: FragmentListadoRecetasBinding?=null
    private val binding get()= checkNotNull(_binding){
        "La vista no ha sido inicializada"
    }
    private val recetasDataSource = RecetasDataSource()
    private var allRecetas:List<Receta> = emptyList() //Almacenara todas las Receta
    private lateinit var recetaAdapter: RecetaAdapter

    private val args: ListadoRecetasArgs by navArgs() //Safe Args: recibir parametros de Home

    //Cuando se crea el fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //Crea la jerarquia de vistas
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Infla layout
        _binding= FragmentListadoRecetasBinding.inflate(inflater,container,false)

        //Devuelve el layout inflado
        return binding.root
    }

    //Despues de que la vista del fragment  se haya creado
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Muestra el bottom menu
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.VISIBLE

        //Configuramos el RView
        //1. Datos, cargamos las recetas del Json
        val rSource= RecetasDataSource() //Instanciamos RecetasDataSource para que nos coja su metodo loadRecetasJSon()
        allRecetas= rSource.loadRecetasJson(context)
        Log.d(TAG, "Total de recetas cargadas desde JSON: ${allRecetas.size}")

        //Filtrado por categoria:
        val categoria=args.categoria
        Log.d(TAG, "Categoría recibida del argumento: $categoria")

        val filtrado= when (categoria){
            "Dulce" -> allRecetas.filter { it.categoria== "Dulce" }
            "Salado" -> allRecetas.filter { it.categoria== "Salado" }
            else -> {
                Log.w(TAG, "Categoría desconocida recibida: $categoria. Mostrando todas las recetas.")
                allRecetas
            }
        }
        Log.d(TAG, "Recetas en la lista FILTRADA: ${filtrado.size}")

        //2. Instanciamos Adapter, recetasAdapter
        val adapter= RecetaAdapter(filtrado)
        //3.Config LayoutManager para el RView
        binding.fragmentListadoRecetas.layoutManager=LinearLayoutManager(context)
        //Le asignamos el RecetasAdapter
        binding.fragmentListadoRecetas.adapter=adapter

        //Se elimina esto porque nos llevaba directamente a itemReceta y no mostraba la lista
        //findNavController().navigate(R.id.action_listadoRecetas_to_itemReceta)

    }

    //Cuando el fragment esta a punto de ser destruido. Liberamos recursos
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}
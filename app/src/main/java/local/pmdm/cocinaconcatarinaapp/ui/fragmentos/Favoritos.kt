package local.pmdm.cocinaconcatarinaapp.ui.fragmentos

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import local.pmdm.cocinaconcatarinaapp.R
import local.pmdm.cocinaconcatarinaapp.databinding.FragmentFavoritosBinding
import local.pmdm.cocinaconcatarinaapp.db.AppDatabase
import local.pmdm.cocinaconcatarinaapp.db.dao.RecetaFavoritaDAO
import local.pmdm.cocinaconcatarinaapp.db.data.FavoritosItemListener
import local.pmdm.cocinaconcatarinaapp.db.data.RecetasDataSource
import local.pmdm.cocinaconcatarinaapp.model.Receta
import local.pmdm.cocinaconcatarinaapp.ui.adapters.FavoritosAdapter



class Favoritos : Fragment(), FavoritosItemListener {
    private var _binding: FragmentFavoritosBinding? = null
    private val binding get()= checkNotNull(_binding){
        "La vista no ha sido inicializada"
    }

    private val rSource = RecetasDataSource()
    private lateinit var allRecetas: List<Receta>
    private lateinit var recetaFavDao: RecetaFavoritaDAO //Lee los favoritos de la BBDD

    //SharedPreferences. Obtienen el user logueado
    private lateinit var sPrefs: SharedPreferences
    private var userEnSesion: String? = null // Variable para almacenar el email del usuario logueado
    private val S_PREFS = "preferenciasUser" // Nombre del archivo de SharedPreferences
    private val USER_EMAIL = "loggedInUserEmail" //clave user

    private lateinit var favoritosAdapter: FavoritosAdapter

    //se llama al crear el fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentFavoritosBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.VISIBLE

        // Inicializar el RecetaFavoritaDAO y SharedPreferences
        val database = AppDatabase.getDatabase(requireContext()) // Obtiene la instancia de la base de datos
        recetaFavDao = database.recetaFavoritaDAO() // Obtiene la instancia del RecetaFavoritaDAO
        sPrefs = requireActivity().getSharedPreferences(S_PREFS, Context.MODE_PRIVATE) // Usar el nombre constante
        userEnSesion = sPrefs.getString(USER_EMAIL, null) // Usar la clave constante

        val allRecetas: List<Receta> =rSource.loadRecetasJson(context)

        favoritosAdapter= FavoritosAdapter(this)

        binding.fragmentFavoritos.layoutManager=LinearLayoutManager(requireContext())
        binding.fragmentFavoritos.adapter =favoritosAdapter

        lifecycleScope.launch(Dispatchers.IO){
            val email=userEnSesion

            if(email != null){
                recetaFavDao.getFavoriteRecipeIdsForUser(email).collectLatest {
                    recetaFavID ->
                    val recetasFavoritas= allRecetas.filter {
                     favorita ->
                     recetaFavID.contains(favorita.id)
                 }
                 withContext(Dispatchers.Main){
                     favoritosAdapter.submitList(recetasFavoritas)
                 }
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun onItemClick(receta: Receta){
           val navegacion=FavoritosDirections.actionFavoritosToItemReceta(
               idReceta = receta.id,
               titulo = receta.nombre,
            )
        findNavController().navigate(navegacion)

    }




}
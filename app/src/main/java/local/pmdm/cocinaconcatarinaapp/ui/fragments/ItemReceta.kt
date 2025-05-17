package local.pmdm.cocinaconcatarinaapp.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import local.pmdm.cocinaconcatarinaapp.R
import local.pmdm.cocinaconcatarinaapp.databinding.FragmentItemRecetaBinding
import local.pmdm.cocinaconcatarinaapp.db.AppDatabase
import local.pmdm.cocinaconcatarinaapp.db.dao.RecetaFavoritaDAO
import local.pmdm.cocinaconcatarinaapp.db.data.RecetasDataSource
import local.pmdm.cocinaconcatarinaapp.db.entities.RecetasFavoritasEntity
import local.pmdm.cocinaconcatarinaapp.model.Receta

/*
 * Fragment para mostrar los detalles de una receta seleccionada.
 * Muestra la imagen, ingredientes, pasos y permite marcar como favorita o modificarla (falta implementacion)
 */
class ItemReceta : Fragment() {
    private var _binding: FragmentItemRecetaBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "La vista no ha sido inicializada"
        }

    private val rSource = RecetasDataSource()// Carga las recetas del JSON
    private lateinit var recetaFavDAO: RecetaFavoritaDAO //interactuar con la tabla de favoritos en la BDD
    private lateinit var sPrefs: SharedPreferences    // SharedPreferences para obtener el email del usuario logueado
    private var userEnSesion: String? =
        null // Variable para almacenar el email del usuario logueado

    // Variable para mantener una referencia a la receta que se está mostrando
    private var currentReceta: Receta? = null

    private val args: ItemRecetaArgs by navArgs() //Safe Args para obtener el ID de la receta

    //SharedPreferences. Obtienen el user logueado y sus datos
    private val S_PREFS = "preferenciasUser" // Nombre del archivo de SharedPreferences
    private val USER_EMAIL = "loggedInUserEmail" //clave user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemRecetaBinding.inflate(inflater, container, false)

        // Muestra bottom menu
        val bottomNavigationView =
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.VISIBLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa el RecetaFavoritaDAO y SharedPreferences
        val bbdd =
            AppDatabase.getDatabase(requireContext()) // Obtiene la instancia de la base de datos
        recetaFavDAO = bbdd.recetaFavoritaDAO() // Obtiene la instancia del RecetaFavoritaDAO

        sPrefs = requireActivity().getSharedPreferences(S_PREFS, Context.MODE_PRIVATE)
        userEnSesion = sPrefs.getString(USER_EMAIL, null)


        val idReceta = args.idReceta // Accedemos al argumento 'idReceta' recibido
        Log.d("ID receta", "Recibido ID de receta: $idReceta")


        //Cargamos todas las recetas para encontrar la que coincide con el ID
        val allRecetas = rSource.loadRecetasJson(context)

        //Encontramos la receta por id
        val itemRecetaSelecionado = allRecetas.find { it.id == idReceta }
        currentReceta = itemRecetaSelecionado // Guardamos la referencia


        //Mostrar los datos de la receta seleccionada y configurar el botón de favorito
        if (currentReceta != null) { // Solo procedemos si se encontró la receta
            Log.d("Busqueda de receta", "Receta encontrada: ${currentReceta!!.nombre}")

            // 1. Mostrar la imagen principal de la receta
            val img = currentReceta!!.imagenReceta
            if (img != null) {
                val resourceId = binding.imgItemReceta.context.resources.getIdentifier(
                    img, "drawable", binding.imgItemReceta.context.packageName
                )
                if (resourceId != 0) {
                    binding.imgItemReceta.setImageResource(resourceId)
                } else {
                    Log.e("Imagen de receta", "Recurso drawable no encontrado para el nombre: $img")
                    binding.imgItemReceta.setImageResource(R.drawable.imgejemplo) // Reemplaza con tu imagen por defecto
                }
            }

            // 2. Listado ingredientes
            val ingredientes = currentReceta!!.ingredientes.map { ingrediente ->
                val cantidad = ingrediente.cantidad?.toString() ?: "A ojo"
                val unidad = ingrediente.unidad?.toString() ?: " sin pasarse"
                "$cantidad $unidad ${ingrediente.nombre}"
            }


            // 3. Asignar el Adapter al listView
            val adapterIngredientes = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, // Un layout simple para cada item de la lista
                ingredientes
            )
            binding.lvIngredientes.adapter = adapterIngredientes

            // SOLUCION a que no se mostraran todos los ingredientes del ListView dentro de un ScrollView:
            setAlturaListView(binding.lvIngredientes)

            // 4. Pasos

            val pasosReceta =
                currentReceta!!.pasos.joinToString(separator = "\n\n") { // Añadido doble salto de línea para mejor legibilidad
                        paso ->
                    "${paso.numero}. ${paso.descripcion}"
                }
            binding.tvPasosReceta.text = pasosReceta


            // 5. Configurar el OnClickListener para el botón de favorito
            binding.ibHacerFavorito.setOnClickListener {
                // Asegurarse de que tenemos la receta y el email del usuario logueado
                val receta = currentReceta
                val userEmail = userEnSesion

                // Solo procedemos si hay una receta cargada Y un usuario logueado
                if (receta != null && userEmail != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        // Verificar el estado actual de favorito en la base de datos
                        val isCurrentlyFavorite =
                            recetaFavDAO.isRecipeFavoriteForUser(userEmail, receta.id)

                        if (isCurrentlyFavorite) {
                            // Si ya es favorito, eliminar de favoritos
                            val favoriteEntity = RecetasFavoritasEntity(userEmail, receta.id)
                            recetaFavDAO.deleteFavoriteRecipe(favoriteEntity)

                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Eliminado de favoritos.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d(
                                    "Favoritos",
                                    "Receta ${receta.id} eliminada de favoritos para ${userEmail}"
                                )
                            }
                        } else {
                            // Si no es favorito, añadir a favoritos
                            val favoriteEntity = RecetasFavoritasEntity(userEmail, receta.id)
                            recetaFavDAO.insertFavoriteRecipe(favoriteEntity)

                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "¡Añadido a favoritos!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d(
                                    "Favoritos",
                                    "Receta ${receta.id} añadida a favoritos para ${userEmail}"
                                )
                            }
                        }
                    }
                } else {
                    // Esto puede pasar si el botón está visible pero no hay usuario logueado
                    Log.e("User login", "Error: Click en favorito sin receta o usuario logueado.")
                    if (userEmail == null) {
                        Toast.makeText(
                            requireContext(),
                            "Inicia sesión para marcar favoritos.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }


            // Configurar el OnClickListener para el botón de modificación
            binding.ibModificar.setOnClickListener {
                // Navegar al fragmento de modificación de receta
                val action = ItemRecetaDirections.actionItemRecetaToModificarReceta(idReceta)
                findNavController().navigate(action)
            }
        }
    }

    /*
     * Establece la altura correcta para el ListView.
     */
    private fun setAlturaListView(lView: ListView) {
        val listAdapter: ListAdapter = lView.adapter ?: return
        var alturaTotal = 0
        val numeroIngredientes = listAdapter.count

        //Calculamos la alturaTotal sumando la altura de cada item
        for (i in 0 until numeroIngredientes) {
            val ingrediente: View = listAdapter.getView(i, null, lView)
            ingrediente.measure(0, 0)
            alturaTotal += ingrediente.measuredHeight
        }

        if (numeroIngredientes > 0) { // Solo sumar divisores si hay items
            alturaTotal += lView.dividerHeight * (numeroIngredientes - 1)
        }

        // Establece la altura calculada al ListView
        val params = lView.layoutParams
        params.height = alturaTotal
        lView.layoutParams = params
        lView.requestLayout() // Solicita que el layout se actualice
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}

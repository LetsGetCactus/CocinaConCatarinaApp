package local.pmdm.cocinaconcatarinaapp.ui.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import local.pmdm.cocinaconcatarinaapp.R
import local.pmdm.cocinaconcatarinaapp.databinding.FragmentRegistroUsuarioBinding
import local.pmdm.cocinaconcatarinaapp.db.AppDatabase
import local.pmdm.cocinaconcatarinaapp.db.dao.UserDAO
import local.pmdm.cocinaconcatarinaapp.db.entities.UserEntity
import java.util.regex.Pattern

/*
 * Fragment para el registro de usuarios
 */
class RegistroUsuario : Fragment() {
    private var _binding: FragmentRegistroUsuarioBinding? = null
    private val binding get() = checkNotNull(_binding){
        "No se a incializado la vista"
    }
    private val args:RegistroUsuarioArgs by navArgs() //Para safe args

    // Instancia del UserDAO para interactuar con la tabla de usuarios
    private lateinit var userDao: UserDAO

    // SharedPreferences para guardar el email del usuario logueado después del registro
    private lateinit var sharedPreferences: SharedPreferences
    //Nombres para SharedPreferences -
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
        _binding= FragmentRegistroUsuarioBinding.inflate(inflater,container,false)

        // Ocultar el BottomNavigationView al crear la vista
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el UserDAO y SharedPreferences
        // Usamos requireContext() para obtener un contexto no nulo
        val database = AppDatabase.getDatabase(requireContext()) // Obtiene la instancia de la base de datos
        userDao = database.userDao() // Obtiene la instancia del UserDAO

        sharedPreferences = requireActivity().getSharedPreferences(S_PREFS, Context.MODE_PRIVATE) // Inicializa SharedPreferences

        //Obtenemos los safe args
        val correo = args.correo
        val pass = args.password
        //Establecemos los EditText con los valores obtenidos
        binding.etCorreo.setText(correo)
        binding.etContraseA.setText(pass)

        binding.btRegistro2.setOnClickListener {
            //Almacenamos los datos en variables por si el user modifica el editText en la iu
            val user= binding.etNombre.text.toString().trim()
            val pass1 = binding.etContraseA.text.toString().trim()
            val pass2 = binding.etContraseA2.text.toString().trim()
            val correo = binding.etCorreo.text.toString().trim()
            if (pass1.isNotBlank() && pass1.equals(pass2) && validarEmail(correo) && user.isNotEmpty()) {
                val password = pass1 //Esto es solo un placeholder

                // Las operaciones de base de datos (inserción) deben ejecutarse en una corrutina
                CoroutineScope(Dispatchers.IO).launch { // Inicia una corrutina
                    // Crear una nueva entidad de usuario
                    val newUser = UserEntity(
                        correo = correo, // Usamos el email como clave primaria
                        nombre = user,
                        contrasenha = password
                    )

                    // Intentar insertar el nuevo usuario en la base de datos
                    // insertUser retorna el rowId (Long) del nuevo usuario insertado,
                    // o -1L si la inserción falló
                    val result = userDao.insertUser(newUser)

                    withContext(Dispatchers.Main) {
                        if (result != -1L) { // Si la inserción fue exitosa (Room retorna el rowId > -1)
                            Toast.makeText(
                                requireContext(),
                                "Usuario registrado con éxito. ¡Bienvenido, $user!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(TAG, "Nuevo usuario registrado: ${newUser.correo}")

                            // Guardar el email del nuevo usuario logueado automáticamente después del registro
                            with(sharedPreferences.edit()) {
                                putString(USER_EMAIL, newUser.correo) // Guarda el email del nuevo usuario
                                apply()
                            }
                            Log.d(TAG, "Usuario logueado automáticamente después del registro: ${newUser.correo}")

                            // Navegar a la pantalla principal (Home)
                            findNavController().navigate(R.id.action_registroUsuario_to_home) // Asegúrate de que esta acción existe en tu nav_graph.xml

                        } else {
                            // Esto suele ocurrir si el email (clave primaria) ya existe debido a OnConflictStrategy.IGNORE
                            Toast.makeText(
                                requireContext(),
                                "Error en el registro. El email ya está en uso.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.w(TAG, "Intento de registro con email existente: $correo")
                        }
                    }
                }
            }
        }
    }

    /*
    * Metodo para validar el email
     */
    private fun validarEmail(email:String): Boolean{
        val pattern= Pattern.compile("[a-z0-9._-]+@[a-z]+\\.+[a-z]{2,}")
        return pattern.matcher(email).matches()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding =null
    }
}
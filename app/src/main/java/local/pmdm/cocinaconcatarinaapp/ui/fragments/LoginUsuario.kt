package local.pmdm.cocinaconcatarinaapp.ui.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import local.pmdm.cocinaconcatarinaapp.R
import local.pmdm.cocinaconcatarinaapp.databinding.FragmentLoginUsuarioBinding
import local.pmdm.cocinaconcatarinaapp.db.AppDatabase
import local.pmdm.cocinaconcatarinaapp.db.dao.UserDAO
import java.util.regex.Pattern

/*
 * Fragment para el login de usuarios.
 */
class LoginUsuario : Fragment() {
    private var _binding: FragmentLoginUsuarioBinding?=null
    private val binding get()= checkNotNull(_binding){
        "La vista no ha sido inicializada"
    }
    // Instancia del UserDAO para interactuar con la tabla de usuarios
    private lateinit var userDao: UserDAO

    // SharedPreferences para guardar el email del usuario logueado
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
        _binding= FragmentLoginUsuarioBinding.inflate(inflater,container,false)

        // Ocultar el BottomNavigationView al crear la vista
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = GONE

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inicializamos UserDAO y SharedPreferences
        val bbdd=AppDatabase.getDatabase(requireContext()) //Instancia de la BBDD
        userDao=bbdd.userDao()
        sharedPreferences =requireActivity().getSharedPreferences(S_PREFS,Context.MODE_PRIVATE)

        binding.btLogin.setOnClickListener {
            val email=binding.etEmail.text.toString().trim()
            val pass=binding.etPass.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa mail y la contraseña.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Sale del listener si los campos están vacíos
            }

            // Las operaciones de base de datos deben ejecutarse en una corrutina
            CoroutineScope(Dispatchers.IO).launch {
                // Buscar el usuario por email en la base de datos
                val user = userDao.getUserByEmail(email)

                withContext(Dispatchers.Main) {
                    if (user != null) {
                        // FALTARIA: Implementar metodos para contraseña segura
                        if (user.contrasenha == pass) { // ¡Recuerda que deberías comparar HASHES!
                            Toast.makeText(requireContext(), "Login exitoso. ¡Bienvenido, ${user.nombre}!", Toast.LENGTH_SHORT).show()

                            // Guardar el email del usuario logueado en SharedPreferences
                            with(sharedPreferences.edit()) {
                                putString(USER_EMAIL, user.correo) // Guarda el email
                                apply() // Aplica los cambios
                            }
                            Log.d(TAG, "Usuario logueado: ${user.correo}")

                            // Navegar a la pantalla principal
                            findNavController().navigate(R.id.action_loginUsuario_to_home)

                        } else {
                            Toast.makeText(requireContext(), "Contraseña incorrecta.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Usuario no encontrado.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            Log.d("LoginUsuario", "Botón de Login clickeado")
        }

        binding.btRegistro.setOnClickListener{
            //Obtener los datos (si los hay) de los edit text para pasarselos al fragment RegistroUsuario si se pulsa el boton de registro
            //(previamente definidos en el fragment RegistroUsuario)
                val correoUser = binding.etEmail.text.toString()
                val passUser = binding.etPass.text.toString()

                if(validarEmail(correoUser)) {
                val accion = LoginUsuarioDirections.actionLoginUsuarioToRegistroUsuario(
                    correo = correoUser,
                    password = passUser
                )

            findNavController().navigate(accion)
            Log.d("LoginUsuario", "Botón de Registro clickeado")
                }else Toast.makeText(requireContext(),"Por favor,introduce un email valido",Toast.LENGTH_SHORT).show()
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
        _binding=null
    }

}
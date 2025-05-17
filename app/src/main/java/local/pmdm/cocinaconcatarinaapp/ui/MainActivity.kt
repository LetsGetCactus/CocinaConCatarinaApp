package local.pmdm.cocinaconcatarinaapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import local.pmdm.cocinaconcatarinaapp.R
import local.pmdm.cocinaconcatarinaapp.databinding.ActivityMainBinding
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.core.content.edit

/*
* Clase principal de la app
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navView: NavigationView

    private var radio:ExoPlayer? =null //Variable para ExoPlayer
    private val rockFM ="https://rockfm-cope.flumotion.com/playlist.m3u8" //URL para el streaming de rockFM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false // Ajusta según el tema
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        navView = binding.navView
        bottomNavigationView = binding.bottomNavigationView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        /*
         * Configuración de la barra de acción y el menú de navegación
         */
        appBarConfiguration = AppBarConfiguration(
            setOf(// IDs de los destinos de nivel superior en tu NavGraph
                R.id.home,
                R.id.favoritos,
                R.id.loginUsuario

            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        bottomNavigationView.setupWithNavController(navController) //Lo eliminamos de aqui porque a veces genera conflicto y no se mueve al fragment al hacer clic sobre el

        /*
         * Configuración de la navegación del menú lateral (Drawer)
         */
        navView.setNavigationItemSelectedListener {
            seleccion ->

            val prefs = getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE)
            val userId = prefs.getString("userId", null)

            when (seleccion.itemId) {
                R.id.home -> {
                    if(userId==null){
                        Toast.makeText(this,"Necesita iniciar sesión",Toast.LENGTH_SHORT).show()
                        false
                    }else {
                        navController.navigate(R.id.home)
                        true
                    }
                }
                R.id.favoritos -> {
                    if(userId==null){
                        Toast.makeText(this,"Necesita iniciar sesión",Toast.LENGTH_SHORT).show()
                        false
                    }else {
                        navController.navigate(R.id.favoritos)
                        true
                    }
                }
                R.id.loginUsuario -> {
                    navController.navigate(R.id.loginUsuario)
                    true
                }
                R.id.logoutUsuario ->{
                cerrarSesion()
                true
                }
                else ->false
            }

        }

        /*
         * Configuración de la navegación de la barra inferior
         */
        bottomNavigationView.setOnItemSelectedListener {
            item ->
            when (item.itemId) {
                R.id.iSonido -> {
                    escucharRadio(item)
                    true
                }
                R.id.favoritos -> {
                    if (navController.currentDestination?.id != R.id.favoritos) {
                        navController.navigate(R.id.favoritos)
                    }
                    true
                }
                R.id.home -> {
                    if (navController.currentDestination?.id != R.id.home) {
                        navController.navigate(R.id.home)
                    }
                    true

                }
                else -> false
            }
        }

        /*
         * Configuración de la navegación de la toolbar
         */
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Para que la Toolbar no muestre los nombres del fragmeto a mostrar:
        navController.addOnDestinationChangedListener{
            _,destination, arguments ->
            when(destination.id) {

                R.id.home -> toolbar.title = "Cocina con Catarina"
                R.id.itemReceta -> {
                    val tituloReceta = arguments?.getString("titulo")
                    if (!tituloReceta.isNullOrEmpty()) {
                        toolbar.title = tituloReceta
                    }
                }
                else -> toolbar.title= ""
            }
        }
    }

    /*
     * Metodo para cerrar sesión del usuario y volver a la pantalla de login
     */
    private fun cerrarSesion() {
        // Limpiar SharedPreferences
        val prefs = getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE)
        prefs.edit { clear() }

        // Redirigir al usuario a la pantalla de login
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


    /*
     * Metodo para controlar los clicks en la toolbar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController.navigateUp(appBarConfiguration) || super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*
     * Metodo para controlar el botón de retroceso
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /*
     * Metodo para inicializar el reproductor de radio
     */
    private fun inicializarRadio(){
       radio= ExoPlayer.Builder(this).build()
        Log.d("ExoPlayer", "Inicializando ExoPlayer")


        val streaming= MediaItem.fromUri(rockFM) //Establecemos RockFm como url a reproducir
        radio?.setMediaItem(streaming) //se la asignamos a la radio/reproductor
        radio?.prepare() //No reproduce, solo carga en MediaItem (streaming) y lo prepara
        Log.d("ExoPlayer", "ExoPlayer inicializado")

    }

    //Play/Pause al clicar en sonido
    private fun escucharRadio(boton: MenuItem){
        if(radio==null) inicializarRadio()
        radio?.let {
            exoPlayer ->
            if(exoPlayer.isPlaying){
                exoPlayer.pause()
                boton.setIcon(R.drawable.radio)
                Toast.makeText(this,"Shhhhhh",Toast.LENGTH_SHORT).show()
            }else {
            exoPlayer.play()
            boton.setIcon(R.drawable.rock)
            Toast.makeText(this,"Rock & roll",Toast.LENGTH_SHORT).show()
        }
        }?: run {
            // Esto solo debería ocurrir si initializeRadioPlayer falla justo antes
            Toast.makeText(this, "ERROR: Reproductor de radio no disponible", Toast.LENGTH_SHORT).show()
            Log.e("ExoPlayer", "No se ha podido escucharRadio() porque radio es NULL")
            boton.setIcon(R.drawable.radio)
        }
    }
    // Liberar recursos
    private fun liberaRadio() {
        if (radio != null) {
            Log.d("RadioPlayer", "Liberando ExoPlayer")
            radio?.release()
            radio = null // Limpiar la referencia
            // }
            Log.d("ExoPlayer", "Liberando ExoPlayer")
        }
    }


    override fun onStart() {
        super.onStart()
        inicializarRadio() //Al iniciar la app, el reproductor estara preparado
    }

    override fun onStop() {
        super.onStop()
        liberaRadio() //Detiene la radio y libera los recursos cuando se cambia de App
    }

}
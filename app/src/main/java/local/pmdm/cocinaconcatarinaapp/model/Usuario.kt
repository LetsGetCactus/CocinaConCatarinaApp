package local.pmdm.cocinaconcatarinaapp.model

/*
Define al usuario de la app, su id de usuario, nomrbe y aquellas recetas que haya dado favoritos
 */
data class Usuario(
    val email:String, //Para identificiacion unica de cada user
    val nombre: String,
    val contrasena: String,
    var favoritosUsuario: MutableSet<Int>? = mutableSetOf() //SET para evitar duplicados, INT para almacenar los id de cada Receta
)

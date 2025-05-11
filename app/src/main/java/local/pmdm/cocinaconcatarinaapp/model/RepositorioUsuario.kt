package local.pmdm.cocinaconcatarinaapp.model

interface RepositorioUsuario {
    fun getUsuario(id: String): Usuario?
    fun agregarUsuario(usuario: Usuario, contrasena: String): Boolean
    fun anhadirAFavoritos(idUsuario: String, idReceta: Int): Boolean
    fun eliminarDeFavoritos(idUsuario: String, idReceta: Int): Boolean
    fun verFavoritos(idUsuario: String): Set<Receta>
}
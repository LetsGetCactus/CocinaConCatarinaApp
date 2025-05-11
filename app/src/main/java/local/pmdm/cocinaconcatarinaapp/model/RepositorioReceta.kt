package local.pmdm.cocinaconcatarinaapp.model

/*
interfaz para definir las operaciones a realizar sobre las recetas
 */
interface RepositorioReceta{
    fun verTodasRecetas(): List<Receta>
    fun buscarRecetaPorCategoria(categoria: String): List<Receta> //Mostrara categoria dulce o salada
    fun modificarReceta(idReceta:Int, nombreIngrediente: String, nuevaCantidad:Int)

}
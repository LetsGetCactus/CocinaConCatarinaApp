package local.pmdm.cocinaconcatarinaapp.model

import local.pmdm.cocinaconcatarinaapp.db.data.RecetasDataSource

/*
* Define la interfaz para las operaciones a realizar sobre las recetas
 */
interface RepositorioReceta{
    fun verTodasRecetas(): List<Receta>
    fun buscarRecetaPorCategoria(categoria: String): List<Receta> //Mostrara categoria dulce o salada
    fun modificarReceta(idReceta:Int, nombreIngrediente: String, nuevaCantidad: Double)
    fun obtenerRecetaPorId(recetaId: Int): Receta

}
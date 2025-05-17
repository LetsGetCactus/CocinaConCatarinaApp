package local.pmdm.cocinaconcatarinaapp.model

import android.content.Context
import local.pmdm.cocinaconcatarinaapp.db.data.RecetasDataSource

/*
 * Implementacion de la interfaz RepositorioReceta
 * para que el ViewModel pueda acceder a los datos (RecetasDataSource) de las recetas
 */
class RepositorioRecetaImpl(private val context: Context): RepositorioReceta {
    private val recetasDataSource: RecetasDataSource = RecetasDataSource()

    override fun verTodasRecetas(): List<Receta> {
        return recetasDataSource.loadRecetasJson(context)
    }

    override fun buscarRecetaPorCategoria(categoria: String): List<Receta> {
      return verTodasRecetas().filter { it.categoria == categoria }
    }

    override fun modificarReceta(idReceta: Int, nombreIngrediente: String, nuevaCantidad: Double) {
        val recetas = verTodasRecetas().toMutableList()
        recetas.find {
            it.id == idReceta
        }?.let { receta ->
            receta.ingredientes.find {
                it.nombre.equals(nombreIngrediente, ignoreCase = true)
            }?.cantidad = nuevaCantidad
        }
    }

    override fun obtenerRecetaPorId(recetaId: Int): Receta {
       return verTodasRecetas().find {
           it.id == recetaId
       }?: throw IllegalArgumentException("Receta no encontrada")
    }
}
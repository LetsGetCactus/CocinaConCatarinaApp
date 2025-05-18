package local.pmdm.cocinaconcatarinaapp.db.dao

import androidx.room.Dao
import androidx.room.Query
import local.pmdm.cocinaconcatarinaapp.db.entities.IngredienteEntity

/*
* Interfaz de acceso a datos (DAO) para la entidad Ingrediente
 */
@Dao
interface IngredienteDao {
    @Query("SELECT * FROM ingredientes WHERE recetaId = :recetaId")
    suspend fun getIngredientesDeReceta(recetaId: Int): List<IngredienteEntity>

    @Query("UPDATE ingredientes SET cantidad = :nuevaCantidad, unidad = :nuevaUnidad WHERE recetaId = :recetaId AND nombre = :nombreIngrediente")
    suspend fun modificarIngredienteDeReceta(recetaId: Int, nombreIngrediente: String, nuevaCantidad: String?, nuevaUnidad: String?)
}
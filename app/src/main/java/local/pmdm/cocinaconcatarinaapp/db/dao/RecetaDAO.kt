package local.pmdm.cocinaconcatarinaapp.db.dao

import androidx.room.Dao
import androidx.room.Query
import local.pmdm.cocinaconcatarinaapp.db.entities.RecetaEntity

/*
Interfaz para interactuar con las tablas de ROOM para recetas
 */
@Dao
interface RecetaDAO {
    @Query("SELECT * FROM recetas")
    suspend fun getRecetas(): List<RecetaEntity>

    @Query("SELECT * FROM recetas WHERE nombre LIKE '%' || :query || '%' OR id IN (SELECT recetaId FROM ingredientes WHERE nombre LIKE '%' || :query || '%')")
    suspend fun buscarRecetas(query: String): List<RecetaEntity>

    @Query("SELECT * FROM recetas WHERE categoria = 'dulce'")
    suspend fun getDulces(): List<RecetaEntity>

    @Query("SELECT * FROM recetas WHERE categoria = 'salada'")
    suspend fun getSaladas(): List<RecetaEntity>


    @Query("SELECT * FROM recetas WHERE favorita = 1")
    suspend fun obtenerRecetasFavoritas(): List<RecetaEntity>

}
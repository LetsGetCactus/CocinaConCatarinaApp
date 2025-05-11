package local.pmdm.cocinaconcatarinaapp.db.dao

import androidx.room.Dao
import androidx.room.Query
import local.pmdm.cocinaconcatarinaapp.repository.room.PasosEntity

@Dao
interface PasoDAO {
    @Query("SELECT * FROM pasos WHERE recetaId = :recetaId ORDER BY numero ASC")
    suspend fun obtenerPasosPorRecetaId(recetaId: Int): List<PasosEntity>

}
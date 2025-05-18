package local.pmdm.cocinaconcatarinaapp.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import local.pmdm.cocinaconcatarinaapp.db.entities.RecetasFavoritasEntity

/*
* Interfaz de acceso a datos (DAO) para la entidad RecetasFavoritas
 */
@Dao
interface RecetaFavoritaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoriteRecipe: RecetasFavoritasEntity)

    @Delete
    suspend fun deleteFavoriteRecipe(favoriteRecipe: RecetasFavoritasEntity)

    @Query("SELECT * FROM recetasFavoritas WHERE correo = :userEmail")
    fun getFavoriteRecipesForUser(userEmail: String): Flow<List<RecetasFavoritasEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM recetasFavoritas WHERE correo = :userEmail AND idReceta = :recipeId LIMIT 1)")
    suspend fun isRecipeFavoriteForUser(userEmail: String, recipeId: Int): Boolean

    @Query("SELECT idReceta FROM recetasFavoritas WHERE correo = :userEmail")
    fun getFavoriteRecipeIdsForUser(userEmail: String): Flow<List<Int>>
}
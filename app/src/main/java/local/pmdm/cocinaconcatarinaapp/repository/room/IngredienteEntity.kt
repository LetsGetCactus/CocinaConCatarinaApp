package local.pmdm.cocinaconcatarinaapp.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "ingredientes",
    primaryKeys = ["recetaId", "nombre"],
    foreignKeys = [ForeignKey(
        entity = RecetaEntity::class,
        parentColumns = ["id"],
        childColumns = ["recetaId"],
        onDelete = ForeignKey.CASCADE)]
)
data class IngredienteEntity(
    @ColumnInfo(name = "recetaId") val recetaId: Int,
    val nombre: String,
    val cantidad: Double,
    val unidad: String
)
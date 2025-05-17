package local.pmdm.cocinaconcatarinaapp.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/*
 Modelo de datos de la BBDD local para Ingrediente
 */
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
package local.pmdm.cocinaconcatarinaapp.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/*
* Modelo de datos de la BBDD local para Pasos
 */
@Entity(
    tableName = "pasos",
    primaryKeys = ["recetaId", "numero"],
    foreignKeys = [ForeignKey(
        entity = RecetaEntity::class,
        parentColumns = ["id"],
        childColumns = ["recetaId"],
        onDelete = ForeignKey.CASCADE)]
)
data class PasosEntity(
    @ColumnInfo(name = "recetaId") val recetaId: Int,
    val numero: Int,
    val texto: String
)
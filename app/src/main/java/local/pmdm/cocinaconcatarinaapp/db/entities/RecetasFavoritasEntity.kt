package local.pmdm.cocinaconcatarinaapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.ColumnInfo


/*
 Modelo de datos de la BBDD local para una receta favorita en la sesion de un usuario
 */
@Entity(
    tableName = "recetasFavoritas",
    // Definimos una clave primaria compuesta por userEmail y recipeId
    // Esto asegura que un usuario (identificado por su email) no pueda
    // marcar la misma receta (identificada por su ID) como favorita varias veces.
    primaryKeys = ["correo", "idReceta"],
    // Definimos una clave foránea para asegurar que el userEmail en esta tabla
    // siempre se refiera a un usuario existente en la tabla 'users'.
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class, // La entidad a la que se refiere la clave foránea (tu entidad de usuario)
            parentColumns = ["email"], // La columna en la entidad padre (el email en UserEntity)
            childColumns = ["correo"], // La columna en esta entidad que referencia al padre
            onDelete = ForeignKey.CASCADE // Si se elimina un usuario, se eliminan sus recetas favoritas asociadas
        )
    ],
    // Añadimos un índice en idReceta para búsquedas más rápidas de recetas favoritas por ID de receta
    indices = [Index(value = ["idReceta"])]
)
data class RecetasFavoritasEntity(
    // Clave foránea que referencia al usuario que marcó la receta como favorita.
    @ColumnInfo(name = "correo")
    val correo: String,

    // El ID de la receta del JSON.
    @ColumnInfo(name = "idReceta")
    val idReceta: Int

)

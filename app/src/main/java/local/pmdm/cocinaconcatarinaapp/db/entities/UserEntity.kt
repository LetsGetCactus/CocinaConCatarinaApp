package local.pmdm.cocinaconcatarinaapp.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index


/*
Entidad para representar la tabla de la BBDD para Usuarios
 */
@Entity(
    tableName = "users", // Nombre de la tabla en la base de datos
    indices = [Index(value = ["user"], unique = true)]
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "email") // Nombre de la columna para correo, que es ademas PK
    val correo: String,

    @ColumnInfo(name = "user") // Nombre de la columna para el nombre de usuario
    val nombre: String,

    @ColumnInfo(name = "pass") // Nombre de la columna para la contraseña
    val contrasenha: String
)

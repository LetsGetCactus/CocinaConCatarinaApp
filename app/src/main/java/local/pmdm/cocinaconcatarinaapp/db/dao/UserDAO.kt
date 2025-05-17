package local.pmdm.cocinaconcatarinaapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import local.pmdm.cocinaconcatarinaapp.db.entities.UserEntity

/*
 DAO para interactuar con la tabla de usuarios (UserEntity)
 */
@Dao
interface UserDAO {
    // Inserta un nuevo usuario en la base de datos.
        @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity): Long

    // Consulta para obtener un usuario por su nombre de usuario (username).
    // LIMIT 1 asegura que solo obtenemos un resultado (o null si no se encuentra).
    @Query("SELECT * FROM users WHERE user = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    // Consulta para obtener un usuario por su ID.
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?


}
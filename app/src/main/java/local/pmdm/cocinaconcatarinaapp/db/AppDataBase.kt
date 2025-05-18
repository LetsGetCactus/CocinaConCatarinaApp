package local.pmdm.cocinaconcatarinaapp.db // Reemplaza con el paquete donde guardarás tu clase de base de datos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import local.pmdm.cocinaconcatarinaapp.db.entities.UserEntity
import local.pmdm.cocinaconcatarinaapp.db.entities.RecetasFavoritasEntity
import local.pmdm.cocinaconcatarinaapp.db.dao.UserDAO
import local.pmdm.cocinaconcatarinaapp.db.dao.RecetaFavoritaDAO // Asegúrate de que el nombre del DAO es correcto

/*
* Clase abstracta que representa y define la estructura de la base de datos de la aplicación.
* BBDD de la app
 */
@Database(
    entities = [UserEntity::class, RecetasFavoritasEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Declara los métodos abstractos para obtener instancias de los DAOs.
    abstract fun userDao(): UserDAO
    abstract fun recetaFavoritaDAO(): RecetaFavoritaDAO

    //PAtron Singleton para obtener la instancia de la BBDD
    companion object {
        // @Volatile asegura que el valor de INSTANCE sea siempre el más actualizado
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /*
         * Metodo para obtener la instancia de la base de datos.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, // La clase de la base de datos
                        "cocina_catarina_db" // Nombre del archivo de la base de datos en el dispositivo
                    ).build()

                    INSTANCE = instance // Asigna la instancia creada a la variable INSTANCE
                    instance // Devuelve la instancia creada
                }
        }
    }
}

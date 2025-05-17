package local.pmdm.cocinaconcatarinaapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
Para usar Room necesitamos una Entidad:
clase de datos que representara las tablas de la BBDD SQLite local

Define la entidad para Receta
 */
@Entity(tableName = "recetas") //Asocia esta data class a la tabla de nombre recetas
data class RecetaEntity(
    @PrimaryKey val id:Int,
    val nombre: String,
    val descripcion: String,
    val tiempoPreparacion: Int,
    val porciones: Int,
    val categoria: String,
    val imagenReceta: Int,
    val favorita: Boolean = false
    //ingredientes y pasos no se añaden aqui porque se gestionaran en otra tabla

)


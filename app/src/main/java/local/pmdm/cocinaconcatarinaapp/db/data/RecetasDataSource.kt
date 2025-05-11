package local.pmdm.cocinaconcatarinaapp.db.data

import android.content.ContentValues.TAG
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import local.pmdm.cocinaconcatarinaapp.model.Receta
import java.io.InputStream
import android.util.Log

/*
Saca los datos del archivo recetas.json
 */
class RecetasDataSource {

    //Cargamos la lista de recetas del archivo json (en res>raw)
    fun loadRecetasJson(context: Context?): List<Receta>{
        Log.d(TAG, "Iniciando carga de recetas desde JSON.")
        val listadoAllRecetas: MutableList<Receta> = mutableListOf()
        var json: String? = null //Almacenara el contenido

        // Verificamos si el contexto es nulo antes de intentar acceder a assets donde tenemos el recetas.json
        if (context == null) {
            Log.e(TAG, "Context es nulo, no se pueden cargar recetas.")
            return emptyList()
        }
        try{
            Log.d(TAG, "Intentando abrir recetas.json desde assets.")
            //Abrimos el json
            val input:InputStream = context.assets.open("recetas.json")
            val tamanho: Int= input.available() ?: 0 //Tamaño del archivo
            Log.d(TAG, "Tamaño del archivo: $tamanho bytes.")
            //Lo leemos a traves de un buffer
            val buffer = ByteArray(tamanho) //Creamos un buffer del tamaño del archivo
            input.read(buffer)
            input.close()
            Log.d(TAG, "Contenido del archivo leído en buffer.")

            //Convertimos el buffer de Bytes a String
            json= String(buffer,Charsets.UTF_8)
            Log.d(TAG, "Contenido del archivo convertido a String (primeros 100 chars): ${json.take(100)}...")
            //Parseamos el String de Json a una List<Receta>
            val gson=Gson()
            val tipo= object : TypeToken<List<Receta>>() {}.type
            Log.d(TAG, "Intentando parsear JSON a List<Receta>.")
            val cargarRecetas: List<Receta> = gson.fromJson(json, tipo)
            Log.d(TAG, "JSON parseado con éxito.")

            //Añadimos las recetas cargadas al Gson a listadoAllRecetas
            listadoAllRecetas.addAll(cargarRecetas)
            Log.d(TAG, "Número de recetas cargadas y añadidas a la lista: ${listadoAllRecetas.size}")
        }catch (exception: Exception){
                exception.printStackTrace()
            return emptyList()
            }

            return listadoAllRecetas
        }
    }

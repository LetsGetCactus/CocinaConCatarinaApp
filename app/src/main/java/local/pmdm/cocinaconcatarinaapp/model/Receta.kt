package local.pmdm.cocinaconcatarinaapp.model

/*
POJO
Representa las diferentes recetas que tienen un id unico por el que se pueden diferenciar
 */
data class Receta(
    val id:Int, //Para identificar cada receta de forma unica
    val nombre: String,
    val descripcion:String,
    val ingredientes: List<Ingrediente> = emptyList(), //Necesitamos que sea List para poder modificarla mas facilemnte (en origen era set para eliminar duplicados)
    val pasos: List<Paso> = emptyList(), //Para añadir la lista ordenada de pasos a seguir
    val tiempo_preparacion: Int,
    val porciones: Int,
    val categoria:String,
    val imagenReceta: String?,
    var favorita:Boolean = false//Por defecto sera falsa salvo que lo activen
)

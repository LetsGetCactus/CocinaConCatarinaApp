package local.pmdm.cocinaconcatarinaapp.model

/*
Representa los ingredientes que se integraran en las diferentes recetas
 */
data class Ingrediente(
    val cantidad: Double,
    val unidad: String?, //Nullable porque algunos ingredientes pueden ser "una pizca", "al gusto", etc
    val nombre:String
)

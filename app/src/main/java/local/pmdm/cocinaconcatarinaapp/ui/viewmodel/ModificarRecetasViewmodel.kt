package local.pmdm.cocinaconcatarinaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import local.pmdm.cocinaconcatarinaapp.model.Receta
import local.pmdm.cocinaconcatarinaapp.model.RepositorioReceta

/*
 * ViewModel para la modificación de recetas
 */
class ModificarRecetasViewmodel(
    private val repositorioReceta: RepositorioReceta
) : ViewModel() {

    private val _recetaEnEdicion = MutableStateFlow<Receta?>(null)
    val recetaEnEdicion: StateFlow<Receta?> = _recetaEnEdicion.asStateFlow()

    // Guardamos una copia original de la receta
    private var recetaOriginal: Receta? = null

    // Carga las recetas
    fun cargarReceta(recetaId: Int){
        //verificación por si ya estuviera cargada
        if(recetaId==_recetaEnEdicion.value?.id) return


        //Asignamos null a la receta en edición por si le hubieramos asignado valor anteriormente
        _recetaEnEdicion.value = null
        recetaOriginal = null

        viewModelScope.launch {
            repositorioReceta.obtenerRecetaPorId(recetaId).let {
                receta ->
                recetaOriginal =receta.copy(ingredientes = receta.ingredientes.map { it.copy() })
                _recetaEnEdicion.value =recetaOriginal
            }
        }
    }

    // Metodo para actualizar la cantidad de un ingrediente
    fun actualizarCantidadIngrediente(position: Int, nuevaCantidad: Double){
        val receta = _recetaEnEdicion.value

        if(position<0 || position >= receta?.ingredientes?.size!!){
            return
        }

        //Creamos la nueva lista de ingrediente
        val actualizarNuevaLista = receta.ingredientes.toMutableList()
        val ingrediente = actualizarNuevaLista.get(position)

        //Nuevo 'ingrediente' con la nueva cantidad
        val actualizarIngrediente = ingrediente.copy(cantidad = nuevaCantidad)
        if (actualizarIngrediente != null) {
            actualizarNuevaLista.set(position, actualizarIngrediente)
        }

        //Actualizamos la receta
        _recetaEnEdicion.value = receta.copy(ingredientes = actualizarNuevaLista)

    }

 /*
  * Metodo para guardar los cambios de la receta
  */
    fun guardarCambios(){
        val recetaModificada = _recetaEnEdicion.value ?: return

        viewModelScope.launch {
            recetaModificada.ingredientes.forEach {
                ingrediente ->
                repositorioReceta.modificarReceta(
                    idReceta = recetaModificada.id,
                    nombreIngrediente = ingrediente.nombre,
                    nuevaCantidad = ingrediente.cantidad
                )
            }
        }
    }

   /*
    * Metodo para resetear la receta a la original del json
    */
    fun resetReceta(){
        recetaOriginal?.let {
            _recetaEnEdicion.value=it.copy(ingredientes = it.ingredientes.map { it.copy() })
        }

    }


}
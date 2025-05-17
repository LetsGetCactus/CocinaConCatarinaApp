package local.pmdm.cocinaconcatarinaapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import local.pmdm.cocinaconcatarinaapp.model.RepositorioReceta

/*
 * Factory para el ViewModel de modificación de recetas
 *
 * @param repositorioReceta Repositorio de Recetas (operaciones a realizar sobre las recetas)
 * @return ModificarRecetasViewmodel
 */
class ModificarRecetasViewmodelFactory(
    private val repositorioReceta: RepositorioReceta
) : ViewModelProvider.Factory {
    /*
     * Crea una instancia del ViewModel
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModificarRecetasViewmodel::class.java)) {
            return ModificarRecetasViewmodel(repositorioReceta) as T

        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}
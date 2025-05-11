package local.pmdm.cocinaconcatarinaapp.db.data

import local.pmdm.cocinaconcatarinaapp.model.Receta

/*
Interfaz para manejar los clics en los items de favoritoscarditem.xml
 */
interface FavoritosItemListener {
    fun onItemClick(receta: Receta)

}
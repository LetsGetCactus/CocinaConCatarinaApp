package local.pmdm.cocinaconcatarinaapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import local.pmdm.cocinaconcatarinaapp.R
import local.pmdm.cocinaconcatarinaapp.databinding.FragmentFavoritoscarditemBinding
import local.pmdm.cocinaconcatarinaapp.model.Receta

/*
* Interfaz para manejar los clics en los items de favoritoscarditem
 */
class FavoritosAdapter(
    private val listener: FavoritosItemListener
) : RecyclerView.Adapter<FavoritosAdapter.FavoritosViewHolder>(){

    //Var para almacenar la lista de Recetas que sean favorito=true
    private var listadoFavoritos: List<Receta> = emptyList()

    /*
     * Actualiza la lista de favoritos y notifica al RView que los datos han cambiado.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Receta>) {
        listadoFavoritos = list // Actualiza la lista interna del adaptador
        notifyDataSetChanged() // Notifica al RView que los datos han cambiado
    }

    class FavoritosViewHolder(
        private val binding: FragmentFavoritoscarditemBinding,
        private val listener: FavoritosItemListener
    ): RecyclerView.ViewHolder(binding.root){
        //Enlaza los datos de una receta con la view
        fun bind(receta: Receta){
            binding.tvNombre.text=receta.nombre
            binding.tvDescripcion.text=receta.descripcion

            val imageName = receta.imagenReceta
            if (imageName != null) {
                val resourceId = itemView.context.resources.getIdentifier(
                    imageName, "drawable", itemView.context.packageName
                )
                if (resourceId != 0) { // resourceId será 0 si el recurso no se encuentra
                    binding.ivFavorito.setImageResource(resourceId) // Establece la imagen usando el ID
                } else {
                    //pone una imagen por defecto si no se encuentra
                    binding.ivFavorito.setImageResource(R.drawable.ic_launcher_foreground)
                }
            } else {
                // Manejar el caso en que imagenReceta es null en el JSON
                binding.ivFavorito.setImageResource(R.drawable.ic_launcher_foreground)
            }
            //Como quiero que todo el cardItem sea boton hacemos binding.root en vez de binding.btIr
            binding.root.setOnClickListener {
               listener.onItemClick(receta)

            }
        }
    }

    //Crear nueva vista del item
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritosAdapter.FavoritosViewHolder {
        val binding = FragmentFavoritoscarditemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoritosViewHolder(binding, listener)
    }

    //Muestra los datos de una posicion establecida
    override fun onBindViewHolder(holder: FavoritosAdapter.FavoritosViewHolder, position: Int) {
        val receta= listadoFavoritos[position]
        holder.bind(receta)
    }

    //Obtener el tamaño de la lista listadoFavoritos
    override fun getItemCount(): Int {
        return listadoFavoritos.size
    }


}
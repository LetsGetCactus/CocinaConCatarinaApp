package local.pmdm.cocinaconcatarinaapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import local.pmdm.cocinaconcatarinaapp.R
import local.pmdm.cocinaconcatarinaapp.databinding.FragmentFavoritoscarditemBinding
import local.pmdm.cocinaconcatarinaapp.db.data.FavoritosItemListener
import local.pmdm.cocinaconcatarinaapp.model.Receta
import local.pmdm.cocinaconcatarinaapp.ui.fragmentos.Favoritos

class FavoritosAdapter(
    private val listener: FavoritosItemListener
) : RecyclerView.Adapter<FavoritosAdapter.FavoritosViewHolder>(){

    //Var para almacenar la lista de Recetas que sean favorito=true
    private var listadoFavoritos: List<Receta> = emptyList()
    // Método para actualizar la lista de recetas en el adaptador
    // Cuando se llama a este método, el RecyclerView se actualizará para mostrar la nueva lista.
    fun submitList(list: List<Receta>) {
        listadoFavoritos = list // Actualiza la lista interna del adaptador
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }

    class FavoritosViewHolder(
        private val binding: FragmentFavoritoscarditemBinding,
        private val listener: FavoritosItemListener
    ): RecyclerView.ViewHolder(binding.root){
        //Enlaza los datos de una receta con la vista
        fun bind(receta: Receta){
            binding.tvNombre.text=receta.nombre
            binding.tvDescripcion.text=receta.descripcion

            val imageName = receta.imagenReceta // Obtiene el nombre del recurso (String?)
            if (imageName != null) {
                // Obtiene el ID del recurso drawable a partir de su nombre
                val resourceId = itemView.context.resources.getIdentifier(
                    imageName, "drawable", itemView.context.packageName
                )
                if (resourceId != 0) { // resourceId será 0 si el recurso no se encuentra
                    binding.ivFavorito.setImageResource(resourceId) // Establece la imagen usando el ID
                } else {
                    // Manejar el caso en que el recurso no se encuentra (ej. poner una imagen por defecto)
                    binding.ivFavorito.setImageResource(R.drawable.ic_launcher_foreground) // Reemplaza con tu imagen por defecto
                }
            } else {
                // Manejar el caso en que imagenReceta es null en el JSON
                binding.ivFavorito.setImageResource(R.drawable.ic_launcher_foreground) // Reemplaza con tu imagen por defecto
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
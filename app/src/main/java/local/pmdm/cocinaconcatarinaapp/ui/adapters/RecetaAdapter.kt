package local.pmdm.cocinaconcatarinaapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import local.pmdm.cocinaconcatarinaapp.R
import local.pmdm.cocinaconcatarinaapp.databinding.CarditemBinding
import local.pmdm.cocinaconcatarinaapp.model.Receta
import local.pmdm.cocinaconcatarinaapp.ui.fragmentos.ListadoRecetasDirections

class RecetaAdapter(private var recetas: List<Receta>):
     RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder>(){

    //ViewHolder, clase anidada que contiene el binding al item a mostrar : CardItem.xml
    class RecetaViewHolder(private val binding: CarditemBinding):
        RecyclerView.ViewHolder(binding.root){
            // Función para enlazar los datos de una receta a las vistas del item
            fun bind(receta: Receta) {
                binding.tvNombreReceta.text = receta.nombre
                val imagen = receta.imagenReceta
                if (imagen != null) {
                    val resourceId = itemView.context.resources.getIdentifier(
                        imagen, "drawable", itemView.context.packageName
                    )
                    if (resourceId != 0) { // resourceId será 0 si el recurso no se encuentra
                        binding.ivImagenReceta.setImageResource(resourceId)
                    } else {
                        // Manejar el caso en que el recurso no se encuentra (ej. poner una imagen por defecto)
                        binding.ivImagenReceta.setImageResource(R.drawable.salado) // Asegúrate de tener una imagen por defecto
                    }
                } else {
                    // Manejar el caso en que imagenReceta es null en el JSON
                    binding.ivImagenReceta.setImageResource(R.drawable.salado)
                }
                // La flecha (ivFlechaAmarilla) no necesita ser actualizada por los datos de la receta
            }

    }

    //Crea una nueva vista del item
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecetaViewHolder {
        val binding= CarditemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecetaViewHolder(binding)
    }

    //Obtiene la posicion actual y muestra los datos de esa receta
    override fun onBindViewHolder(holder:RecetaViewHolder, position: Int) {
        val receta= recetas[position]
        holder.bind(receta) //Enlaza los datos al ViewHolder

        //Listener para navegar al itemReceta seleccionado
        holder.itemView.setOnClickListener{
            val tituloReceta = receta.nombre
            val action=ListadoRecetasDirections.actionListadoRecetasToItemReceta(
                idReceta =  receta.id,
                titulo = tituloReceta)
            holder.itemView.findNavController().navigate(action)
        }
    }

    //Obtiene el tamaño de la lista recetas (constructor clase)
    override fun getItemCount(): Int {
        return recetas.size
    }


}
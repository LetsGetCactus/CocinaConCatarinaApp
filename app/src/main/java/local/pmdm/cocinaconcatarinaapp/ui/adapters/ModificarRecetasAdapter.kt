package local.pmdm.cocinaconcatarinaapp.ui.adapters

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import local.pmdm.cocinaconcatarinaapp.databinding.ItemIngredienteAModificarBinding
import local.pmdm.cocinaconcatarinaapp.model.Ingrediente


/*
 * Adapter para el RecyclerView ModificarRecetas que muestra la lista(cardView) de ingredientes.
 * Recibe una lista de ingredientes y muestra cada uno en un item.
 *
 * @param onIngredienteChanged: Lambda que se llama cuando cambia la cantidad de un ingrediente.
 */
class ModificarRecetasAdapter(
    private val onIngredienteChanged: (Int, Double) -> Unit
): RecyclerView.Adapter<ModificarRecetasAdapter.IngredienteViewHolder>(){

    // Lista de ingredientes que muestra el adaptador
    private var listadoIngredientes: List<Ingrediente> = emptyList()

    // Metodo para actualizar los datos del adapter
    fun actualizarIngredientes(nuevosIngredientes: List<Ingrediente>){
        listadoIngredientes=nuevosIngredientes
        notifyDataSetChanged() //Notificará al RView que los datos han cambiado
    }

   /*
    * ViewHolder (clase anidada del adapter) para cada item de la lista de ingredientes.
    */
    class IngredienteViewHolder (
        private val binding: ItemIngredienteAModificarBinding,
        private val listadoIngredientes: List<Ingrediente>,
        private val onIngredienteChanged: (Int, Double) -> Unit) :RecyclerView.ViewHolder(binding.root) {

        // Para escuchar los cambios del EditText
        private val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            /*
             * Se llama cuando el texto del EditText cambia.
             */
            override fun afterTextChanged(s: Editable?) {
                val nuevaCantidad = s.toString().toDoubleOrNull() ?: 0.0

                val position = absoluteAdapterPosition
                Log.d("ModificarRecetasAdapter", "Posición del ingrediente: $position")
                if (position in listadoIngredientes.indices) {
                    onIngredienteChanged(position, nuevaCantidad)
                } else {
                    Log.e("ModificarRecetasAdapter", "Posición inválida detectada: $position")
                }
            }

        }

        /*
         * Inicializa y enlaza los datos de un ingrediente con la vista correspondiente.
         */
        fun bind(ingrediente: Ingrediente){
            binding.tvNombreIngrediente.text = ingrediente.nombre
            binding.tvUnidadIngrediente.text = ingrediente.unidad
            binding.etCantidadIngrediente.setText(ingrediente.cantidad.toString())
            binding.etCantidadIngrediente.setOnFocusChangeListener{
                    _, hasFocus ->
                if (hasFocus){
                    binding.etCantidadIngrediente.text.clear() //Borra el contenido del editText (hint) al clicar
                }
            }
            binding.etCantidadIngrediente.addTextChangedListener(textWatcher)


        }
    }



    /*
     * Crea una nueva vista del item, devuelve un IngredienteViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredienteViewHolder {
        val binding = ItemIngredienteAModificarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredienteViewHolder(binding, listadoIngredientes, onIngredienteChanged)
    }

    /*
     * Devuelve el número de ingredientes en la lista.
     */
    override fun getItemCount(): Int = listadoIngredientes.size

    /*
     * Enlaza los datos de un ingrediente en una posición específica con el ViewHolder.
     */
    override fun onBindViewHolder(holder: IngredienteViewHolder, position: Int) {
       val ingrediente= listadoIngredientes[position]
        holder.bind(ingrediente)
    }


}
package br.com.marvel.presentation.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.marvel.data.model.CharacterModel
import br.com.marvel.databinding.ItemCharacterBinding

// Adapter para exibir uma lista paginada de CharacterModel em um RecyclerView.
class CharacterAdapter : PagingDataAdapter<CharacterModel, CharacterAdapter.ViewHolder>(CharacterAdapterDiffCallback()) {

    // Vincula os dados do item ao ViewHolder.
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Obtém o item na posição especificada.
        val item = getItem(position)
        // Se o item não for nulo, chama o método de ligação do ViewHolder.
        item?.let { viewHolder.bind(it) }
    }

    // Cria uma nova instância do ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Cria e retorna um novo ViewHolder a partir do layout fornecido.
        return ViewHolder.from(parent)
    }

    // ViewHolder para gerenciar a visualização dos itens de CharacterModel.
    class ViewHolder private constructor(private val itemCharacterBinding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(itemCharacterBinding.root) {

        // Liga o modelo de dados ao layout do item.
        fun bind(characterModel: CharacterModel) {
            // Atribui o modelo de dados ao binding.
            itemCharacterBinding.charactermodel = characterModel
            // Executa todas as atualizações pendentes no layout.
            itemCharacterBinding.executePendingBindings()
        }

        companion object {
            // Cria uma nova instância do ViewHolder a partir do layout especificado.
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemCharacterBinding =
                    ItemCharacterBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(itemCharacterBinding)
            }
        }
    }
}

// DiffUtil para calcular as diferenças entre dois conjuntos de dados e atualizar o RecyclerView de forma eficiente.
class CharacterAdapterDiffCallback : DiffUtil.ItemCallback<CharacterModel>() {

    // Verifica se dois itens são os mesmos com base no ID.
    override fun areItemsTheSame(
        oldItem: CharacterModel, newItem: CharacterModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    // Verifica se o conteúdo dos itens é o mesmo.
    override fun areContentsTheSame(
        oldItem: CharacterModel, newItem: CharacterModel
    ): Boolean {
        return oldItem == newItem
    }
}


package br.com.marvel.presentation.comic

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.marvel.data.model.ComicModel
import br.com.marvel.databinding.ItemComicBinding

class ComicAdapter(
    private val onCharacterItemClick: (ComicModel) -> Unit,
    private val onFavoriteItemClick: (ComicModel) -> Unit,
) : PagingDataAdapter<ComicModel, ComicAdapter.ViewHolder>(ComicAdapterDiffCallback()) {

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Obtém o item na posição atual e o vincula ao ViewHolder
        val item = getItem(position)
        item?.let { viewHolder.bind(it) }
        Log.d("ComicAdapter", "Binding item at position: $position with id: ${item?.id}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Cria uma nova instância do ViewHolder com as callbacks
        return ViewHolder.from(parent, onCharacterItemClick, onFavoriteItemClick)
    }

    class ViewHolder private constructor(
        private val itemComicBinding: ItemComicBinding,
        private val onCharacterItemClick: (ComicModel) -> Unit,
        private val onFavoriteItemClick: (ComicModel) -> Unit
    ) : RecyclerView.ViewHolder(itemComicBinding.root) {

        fun bind(
            comicModel: ComicModel
        ) {
            // Vincula o modelo de quadrinho ao binding
            itemComicBinding.comicmodel = comicModel
            itemComicBinding.executePendingBindings()

            // Configura o clique no botão de personagens
            itemComicBinding.buttonCharacter.setOnClickListener {
                onCharacterItemClick(comicModel)
                Log.d("ComicAdapter", "Character button clicked for comic id: ${comicModel.id}")
            }

            // Configura o clique no botão de favoritos
            itemComicBinding.imagebuttonFavorite.setOnClickListener {
                onFavoriteItemClick(comicModel)
                Log.d("ComicAdapter", "Favorite button clicked for comic id: ${comicModel.id}")
            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onCharacterItemClick: (ComicModel) -> Unit,
                onFavoriteItemClick: (ComicModel) -> Unit
            ): ViewHolder {
                // Infla o layout do item e cria uma nova instância do ViewHolder
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemComicBinding = ItemComicBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(itemComicBinding, onCharacterItemClick, onFavoriteItemClick)
            }
        }
    }
}

class ComicAdapterDiffCallback : DiffUtil.ItemCallback<ComicModel>() {

    override fun areItemsTheSame(
        oldItem: ComicModel, newItem: ComicModel
    ): Boolean {
        // Compara os IDs dos itens para verificar se são o mesmo item
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ComicModel, newItem: ComicModel
    ): Boolean {
        // Compara o conteúdo dos itens para verificar se são iguais
        return oldItem == newItem
    }
}
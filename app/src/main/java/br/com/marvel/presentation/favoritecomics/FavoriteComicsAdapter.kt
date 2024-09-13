package br.com.marvel.presentation.favoritecomics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.marvel.data.source.local.db.entities.FavoriteComicsEntity
import br.com.marvel.databinding.ItemFavoriteComicsBinding

class FavoriteComicsAdapter :
    ListAdapter<FavoriteComicsEntity, FavoriteComicsAdapter.ViewHolder>(FavoriteComicsAdapterDiffCallback()) {

    // Vincula o item da lista ao ViewHolder
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { viewHolder.bind(it) }
    }

    // Cria um novo ViewHolder para o item da lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(
        private val itemFavoriteComicsBinding: ItemFavoriteComicsBinding
    ) : RecyclerView.ViewHolder(itemFavoriteComicsBinding.root) {

        // Vincula a entidade de quadrinho favorito aos elementos da interface do usuário
        fun bind(
            favoriteComicsEntity: FavoriteComicsEntity
        ) {
            itemFavoriteComicsBinding.favoritecomicsentity = favoriteComicsEntity
            itemFavoriteComicsBinding.executePendingBindings()
        }

        companion object {
            // Infla o layout para o ViewHolder
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemFavoriteComicsBinding =
                    ItemFavoriteComicsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(itemFavoriteComicsBinding)
            }
        }
    }
}

// Callback para calcular as diferenças entre duas listas de FavoriteComicsEntity
class FavoriteComicsAdapterDiffCallback : DiffUtil.ItemCallback<FavoriteComicsEntity>() {

    // Compara se os itens são os mesmos (usado para identificar itens únicos)
    override fun areItemsTheSame(
        oldItem: FavoriteComicsEntity, newItem: FavoriteComicsEntity
    ): Boolean {
        return oldItem.id == newItem.id
    }

    // Compara se os conteúdos dos itens são os mesmos (usado para verificar alterações no conteúdo)
    override fun areContentsTheSame(
        oldItem: FavoriteComicsEntity, newItem: FavoriteComicsEntity
    ): Boolean {
        return oldItem == newItem
    }
}
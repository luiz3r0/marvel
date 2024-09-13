package br.com.marvel.domain.usecase.favoritecomics

import br.com.marvel.data.source.local.db.entities.FavoriteComicsEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteComicsUseCaseInterface {

    // Insere um comic na lista de favoritos.
    // Caso o comic já exista, ele será atualizado.
    suspend fun insertFavoriteComicsRoom(favoriteComicsEntity: FavoriteComicsEntity)

    // Remove um comic da lista de favoritos com base no ID fornecido.
    suspend fun deleteFavoriteComicsRoom(comicId: Int?)

    // Retorna um fluxo com a lista de todos os comics favoritos.
    // O fluxo permite a observação e atualização dinâmica da lista.
    fun listFavoriteComicsRoom(): Flow<List<FavoriteComicsEntity>>

    // Verifica se um comic está presente na lista de favoritos com base no ID fornecido.
    suspend fun doesComicExist(comicId: Int?): Boolean

    // Obtém a lista de todos os IDs dos comics favoritos.
    suspend fun getAllFavoriteComicIds(): List<Int>
}
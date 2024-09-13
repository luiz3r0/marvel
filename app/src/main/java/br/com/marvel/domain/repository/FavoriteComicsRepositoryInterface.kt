package br.com.marvel.domain.repository

import br.com.marvel.data.source.local.db.entities.FavoriteComicsEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteComicsRepositoryInterface {

    // Insere um comic na lista de favoritos. Se o comic já existir, ele será atualizado.
    // Função suspensa para operações assíncronas com Room.
    suspend fun insertFavoriteComicsRoom(favoriteComicsEntity: FavoriteComicsEntity)

    // Remove um comic da lista de favoritos baseado no ID fornecido.
    // Função suspensa para operações assíncronas com Room.
    suspend fun deleteFavoriteComicRoom(comicId: Int?)

    // Retorna um fluxo de dados com a lista de todos os comics favoritos.
    // Usado para observar mudanças na lista de favoritos e atualizar a UI conforme necessário.
    fun listFavoriteComicsRoom(): Flow<List<FavoriteComicsEntity>>

    // Verifica se um comic com o ID fornecido existe na lista de favoritos.
    // Função suspensa para operações assíncronas com Room.
    suspend fun doesComicExist(comicId: Int?): Boolean

    // Obtém todos os IDs dos comics favoritos armazenados.
    // Função suspensa para operações assíncronas com Room.
    suspend fun getAllFavoriteComicIds(): List<Int>
}
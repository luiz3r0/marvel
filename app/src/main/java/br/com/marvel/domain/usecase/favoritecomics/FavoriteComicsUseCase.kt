package br.com.marvel.domain.usecase.favoritecomics

import android.util.Log
import br.com.marvel.data.source.local.db.entities.FavoriteComicsEntity
import br.com.marvel.domain.repository.FavoriteComicsRepositoryInterface
import kotlinx.coroutines.flow.Flow

class FavoriteComicsUseCase(
    private val favoriteComicsRepositoryInterface: FavoriteComicsRepositoryInterface
) : FavoriteComicsUseCaseInterface {

    // Adiciona um comic à lista de favoritos.
    // Recebe um objeto `FavoriteComicsEntity` e o insere no banco de dados.
    override suspend fun insertFavoriteComicsRoom(favoriteComicsEntity: FavoriteComicsEntity) {
        Log.d("FavoriteComicsUseCase", "Inserindo comic com ID: ${favoriteComicsEntity.id}")
        favoriteComicsRepositoryInterface.insertFavoriteComicsRoom(favoriteComicsEntity)
    }

    // Remove um comic da lista de favoritos pelo ID.
    // O comic com o ID fornecido será removido do banco de dados, se existir.
    override suspend fun deleteFavoriteComicsRoom(comicId: Int?) {
        Log.d("FavoriteComicsUseCase", "Removendo comic com ID: $comicId")
        favoriteComicsRepositoryInterface.deleteFavoriteComicRoom(comicId)
    }

    // Obtém uma lista de todos os comics favoritos.
    // Retorna um fluxo (`Flow`) contendo a lista de comics favoritos.
    override fun listFavoriteComicsRoom(): Flow<List<FavoriteComicsEntity>> {
        return favoriteComicsRepositoryInterface.listFavoriteComicsRoom()
    }

    // Verifica se um comic com o ID fornecido existe na lista de favoritos.
    // Retorna `true` se o comic existir, caso contrário, `false`.
    override suspend fun doesComicExist(comicId: Int?): Boolean {
        return favoriteComicsRepositoryInterface.doesComicExist(comicId)
    }

    // Obtém todos os IDs dos comics que estão na lista de favoritos.
    // Retorna uma lista contendo todos os IDs dos comics favoritos.
    override suspend fun getAllFavoriteComicIds(): List<Int> {
        return favoriteComicsRepositoryInterface.getAllFavoriteComicIds()
    }
}
package br.com.marvel.data.source.local.repository

import android.util.Log
import br.com.marvel.data.source.local.db.dao.FavoriteComicsDao
import br.com.marvel.data.source.local.db.entities.FavoriteComicsEntity
import br.com.marvel.domain.repository.FavoriteComicsRepositoryInterface
import kotlinx.coroutines.flow.Flow

class FavoriteComicsRepository(private val favoriteComicsDao: FavoriteComicsDao) :
    FavoriteComicsRepositoryInterface {

    // Insere um novo comic ou atualiza se já existir um com o mesmo ID
    override suspend fun insertFavoriteComicsRoom(favoriteComicsEntity: FavoriteComicsEntity) {
        Log.d("FavoriteComicsRepository", "Inserindo ou atualizando comic com ID: ${favoriteComicsEntity.id}")
        favoriteComicsDao.insertFavoriteComicsRoom(favoriteComicsEntity)
        Log.d("FavoriteComicsRepository", "Comic com ID: ${favoriteComicsEntity.id} inserido ou atualizado com sucesso")
    }

    // Remove um comic da lista de favoritos pelo seu ID
    override suspend fun deleteFavoriteComicRoom(comicId: Int?) {
        Log.d("FavoriteComicsRepository", "Removendo comic com ID: $comicId")
        favoriteComicsDao.deleteFavoriteComicsRoom(comicId)
        Log.d("FavoriteComicsRepository", "Comic com ID: $comicId removido com sucesso")
    }

    // Lista todos os comics favoritos ordenados por título
    override fun listFavoriteComicsRoom(): Flow<List<FavoriteComicsEntity>> {
        Log.d("FavoriteComicsRepository", "Listando todos os comics favoritos")
        return favoriteComicsDao.listFavoriteComicsRoom().also {
            Log.d("FavoriteComicsRepository", "Lista de comics favoritos retornada")
        }
    }

    // Verifica se um comic com o ID fornecido existe na lista de favoritos
    override suspend fun doesComicExist(comicId: Int?): Boolean {
        Log.d("FavoriteComicsRepository", "Verificando se comic com ID: $comicId existe")
        val exists = favoriteComicsDao.doesComicExist(comicId)
        Log.d("FavoriteComicsRepository", "Comic com ID: $comicId existe: $exists")
        return exists
    }

    // Obtém todos os IDs dos comics favoritos
    override suspend fun getAllFavoriteComicIds(): List<Int> {
        Log.d("FavoriteComicsRepository", "Obtendo todos os IDs dos comics favoritos")
        val ids = favoriteComicsDao.getAllFavoriteComicIds()
        Log.d("FavoriteComicsRepository", "IDs dos comics favoritos obtidos: $ids")
        return ids
    }
}
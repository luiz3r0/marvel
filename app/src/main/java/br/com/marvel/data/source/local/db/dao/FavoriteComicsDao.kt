package br.com.marvel.data.source.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.marvel.data.source.local.db.entities.FavoriteComicsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteComicsDao {

    // Insere um novo comic ou atualiza se já existir um com o mesmo ID
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteComicsRoom(favoriteComicsEntity: FavoriteComicsEntity)

    // Remove um comic da lista de favoritos pelo seu ID
    @Query("DELETE FROM comicentity WHERE id = :comicId")
    suspend fun deleteFavoriteComicsRoom(comicId: Int?)

    // Lista todos os comics favoritos ordenados por título em ordem crescente
    @Query("SELECT * FROM comicentity ORDER BY title ASC")
    fun listFavoriteComicsRoom(): Flow<List<FavoriteComicsEntity>>

    // Verifica se um comic com o ID fornecido existe na lista de favoritos
    @Query("SELECT EXISTS(SELECT 1 FROM comicentity WHERE id = :comicId)")
    suspend fun doesComicExist(comicId: Int?): Boolean

    // Obtém todos os IDs dos comics favoritos
    @Query("SELECT id FROM comicentity")
    suspend fun getAllFavoriteComicIds(): List<Int>
}
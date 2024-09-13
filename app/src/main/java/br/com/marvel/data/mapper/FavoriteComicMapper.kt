package br.com.marvel.data.mapper

import android.util.Log
import br.com.marvel.data.model.ComicModel
import br.com.marvel.data.source.local.db.entities.FavoriteComicsEntity

object FavoriteComicMapper {

    private const val TAG = "FavoriteComicMapper"

    fun comicModelToEntity(comicModel: ComicModel): FavoriteComicsEntity {
        // Log de entrada com detalhes do ComicModel
        Log.d(TAG, "Converting ComicModel to FavoriteComicsEntity: $comicModel")

        // Verificações e conversão
        val id = comicModel.id ?: 0
        val title = comicModel.title ?: ""
        val description = comicModel.description ?: ""
        val thumbnail = comicModel.thumbnail

        // Log dos valores após a conversão
        Log.d(TAG, "Converted values: id=$id, title=$title, description=$description")

        // Criando e retornando o FavoriteComicsEntity
        return FavoriteComicsEntity(
            id = id,
            title = title,
            description = description,
            thumbnail = thumbnail
        ).also {
            // Log final da entidade criada
            Log.d(TAG, "Created FavoriteComicsEntity: $it")
        }
    }
}
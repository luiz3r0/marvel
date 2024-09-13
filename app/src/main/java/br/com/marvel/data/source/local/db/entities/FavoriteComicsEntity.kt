package br.com.marvel.data.source.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.marvel.data.model.Image

@Entity(tableName = "comicentity")
data class FavoriteComicsEntity(
    // ID do comic favorito, usado como chave primária
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int?,

    // Título do comic favorito
    @ColumnInfo(name = "title")
    val title: String?,

    // Descrição do comic favorito
    @ColumnInfo(name = "description")
    val description: String?,

    // Thumbnail ou imagem do comic favorito
    @ColumnInfo(name = "thumbnail")
    val thumbnail: Image?
)
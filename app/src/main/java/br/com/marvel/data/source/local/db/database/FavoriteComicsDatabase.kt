package br.com.marvel.data.source.local.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.marvel.data.source.local.db.dao.FavoriteComicsDao
import br.com.marvel.data.source.local.db.entities.FavoriteComicsEntity
import br.com.marvel.utils.typeconverter.ConvertersUtils

// Define a classe como um banco de dados Room
@Database(
    entities = [FavoriteComicsEntity::class], // Lista de entidades para o banco de dados
    version = 3, // Versão do banco de dados. Incrementar em caso de mudanças no esquema.
    exportSchema = false // Define se o esquema do banco de dados deve ser exportado para um arquivo de esquema.
)
@TypeConverters(ConvertersUtils::class) // Define a classe para conversão de tipos personalizados
abstract class FavoriteComicsDatabase : RoomDatabase() {
    // Declara um DAO para acesso a comics favoritos
    abstract val favoriteComicsDao: FavoriteComicsDao
}
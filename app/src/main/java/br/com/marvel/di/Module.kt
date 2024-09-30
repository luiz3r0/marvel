package br.com.marvel.di

import android.util.Log
import androidx.room.Room
import br.com.marvel.data.model.ComicModel
import br.com.marvel.data.source.local.db.database.FavoriteComicsDatabase
import br.com.marvel.data.source.local.repository.FavoriteComicsRepository
import br.com.marvel.domain.repository.FavoriteComicsRepositoryInterface
import br.com.marvel.data.source.remote.repository.CharacterRepository
import br.com.marvel.data.source.remote.retrofit.MarvelRetrofitClient
import br.com.marvel.data.source.remote.retrofit.MarvelRetrofitClientInterface
import br.com.marvel.data.source.remote.api.ComicService
import br.com.marvel.data.source.remote.repository.ComicRepository
import br.com.marvel.data.source.remote.api.CharacterService
import br.com.marvel.domain.repository.CharacterRepositoryInterface
import br.com.marvel.domain.repository.ComicRepositoryInterface
import br.com.marvel.domain.usecase.favoritecomics.FavoriteComicsUseCase
import br.com.marvel.domain.usecase.favoritecomics.FavoriteComicsUseCaseInterface
import br.com.marvel.domain.usecase.character.CharacterUseCase
import br.com.marvel.domain.usecase.character.CharacterUseCaseInterface
import br.com.marvel.domain.usecase.comic.ComicUseCase
import br.com.marvel.domain.usecase.comic.ComicUseCaseInterface
import br.com.marvel.presentation.character.CharacterAdapter
import br.com.marvel.presentation.comic.ComicAdapter
import br.com.marvel.presentation.comic.ComicViewModel
import br.com.marvel.presentation.favoritecomics.FavoriteComicsViewModel
import br.com.marvel.presentation.character.CharacterViewModel
import br.com.marvel.presentation.favoritecomics.FavoriteComicsAdapter
import br.com.marvel.utils.hashgenerator.HashGeneratorUtils
import br.com.marvel.utils.hashgenerator.HashGeneratorUtilsInterface
import br.com.marvel.utils.internetavailability.InternetAvailabilityUtils
import br.com.marvel.utils.internetavailability.InternetAvailabilityUtilsInterface
import br.com.marvel.utils.logging.LogcatLoggerUtils
import br.com.marvel.utils.logging.LogcatLoggerUtilsInterface
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Module = module {

    single {
        Log.d("AppModule", "Criando instância do FavoriteComicsDatabase")
        Room.databaseBuilder(
            androidContext(), // Contexto da aplicação
            FavoriteComicsDatabase::class.java,
            "comicdatabase" // Nome do banco de dados
        )
            .fallbackToDestructiveMigration() // Gerencia migrações automaticamente durante o desenvolvimento
            .build()
            .also { Log.d("AppModule", "Instância do FavoriteComicsDatabase criada") }
    }

    single {
        Log.d("AppModule", "Obtendo instância do FavoriteComicsDao")
        get<FavoriteComicsDatabase>().favoriteComicsDao
            .also { Log.d("AppModule", "Instância do FavoriteComicsDao obtida") }
    }

    viewModel {
        ComicViewModel(get(), get(), get())
    }
        .also { Log.d("AppModule", "Instância do ComicViewModel criada") }

    viewModel {
        CharacterViewModel(get(), get())
    }

    viewModel { FavoriteComicsViewModel(get()) }
        .also { Log.d("AppModule", "Instância do FavoriteComicsViewModel criada") }

    single<MarvelRetrofitClientInterface> {
        Log.d("AppModule", "Criando instância do MarvelRetrofitClientInterface")
        MarvelRetrofitClient(androidApplication = androidApplication())
    }.also { Log.d("AppModule", "Instância do MarvelRetrofitClientInterface criada") }

    single {
        Log.d("AppModule", "Criando instância do ComicService Retrofit")
        get<MarvelRetrofitClientInterface>().startRetrofit(ComicService::class.java)
    }.also { Log.d("AppModule", "Instância do ComicService Retrofit criada") }

    single {
        Log.d("AppModule", "Criando instância do CharacterService Retrofit")
        get<MarvelRetrofitClientInterface>().startRetrofit(CharacterService::class.java)
    }.also { Log.d("AppModule", "Instância do CharacterService Retrofit criada") }

    factory<ComicRepositoryInterface> {
        Log.d("AppModule", "Criando instância do ComicRepository")
        ComicRepository(get(), get(), get())
    }.also { Log.d("AppModule", "Instância do ComicRepository criada") }

    factory<FavoriteComicsRepositoryInterface> {
        Log.d("AppModule", "Criando instância do FavoriteComicsRepository")
        FavoriteComicsRepository(get())
    }.also { Log.d("AppModule", "Instância do FavoriteComicsRepository criada") }

    factory<CharacterRepositoryInterface> {
        Log.d("AppModule", "Criando instância do CharacterRepository")
        CharacterRepository(get(), get(), get())
    }.also { Log.d("AppModule", "Instância do CharacterRepository criada") }

    factory<HashGeneratorUtilsInterface> {
        Log.d("AppModule", "Criando instância do HashGeneratorUtils")
        HashGeneratorUtils()
    }.also { Log.d("AppModule", "Instância do HashGeneratorUtils criada") }

    factory<ComicUseCaseInterface> {
        Log.d("AppModule", "Criando instância do ComicUseCase")
        ComicUseCase(get())
    }.also { Log.d("AppModule", "Instância do ComicUseCase criada") }

    factory<FavoriteComicsUseCaseInterface> {
        Log.d("AppModule", "Criando instância do FavoriteComicsUseCase")
        FavoriteComicsUseCase(get())
    }.also { Log.d("AppModule", "Instância do FavoriteComicsUseCase criada") }

    factory<CharacterUseCaseInterface> {
        Log.d("AppModule", "Criando instância do CharacterUseCase")
        CharacterUseCase(get())
    }.also { Log.d("AppModule", "Instância do CharacterUseCase criada") }

    factory { (onCharacterItemClick: (ComicModel) -> Unit, onFavoriteItemClick: (ComicModel) -> Unit) ->
        Log.d("AppModule", "Criando instância do ComicAdapter")
        ComicAdapter(onCharacterItemClick, onFavoriteItemClick)
    }.also { Log.d("AppModule", "Instância do ComicAdapter criada") }

    factory {
        Log.d("AppModule", "Criando instância do FavoriteComicsAdapter")
        FavoriteComicsAdapter()
    }.also { Log.d("AppModule", "Instância do FavoriteComicsAdapter criada") }

    factory {
        Log.d("AppModule", "Criando instância do CharacterAdapter")
        CharacterAdapter()
    }.also {
        Log.d("AppModule", "Instância do CharacterAdapter criada")
    }

    factory<InternetAvailabilityUtilsInterface> {
        Log.d("AppModule", "Criando instância do InternetAvailabilityUtils")
        InternetAvailabilityUtils()
    }.also { Log.d("AppModule", "Instância do InternetAvailabilityUtils criada") }

    factory<LogcatLoggerUtilsInterface> { LogcatLoggerUtils() }
}
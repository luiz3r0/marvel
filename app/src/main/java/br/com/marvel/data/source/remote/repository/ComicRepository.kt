package br.com.marvel.data.source.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.marvel.BuildConfig
import br.com.marvel.data.model.ComicModel
import br.com.marvel.data.source.paging.ComicPagingSource
import br.com.marvel.data.source.remote.api.ComicService
import br.com.marvel.domain.repository.ComicRepositoryInterface
import br.com.marvel.utils.hashgenerator.HashGeneratorUtilsInterface
import br.com.marvel.utils.logging.LogcatLoggerUtilsInterface
import kotlinx.coroutines.flow.Flow

class ComicRepository(
    private val comicService: ComicService,
    private val hashGeneratorUtilsInterface: HashGeneratorUtilsInterface,
    private val logcatLoggerUtilsInterface: LogcatLoggerUtilsInterface // Adiciona o Logger
) : ComicRepositoryInterface {

    private val _errorState = MutableLiveData<Throwable>()
    private val errorState: LiveData<Throwable> = _errorState

    override fun erro(): LiveData<Throwable> {
        return errorState
    }

    override fun getComicsFlow(): Flow<PagingData<ComicModel>> {
        logcatLoggerUtilsInterface.d("ComicRepository", "Iniciando fluxo de HQs")

        return Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 15,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                logcatLoggerUtilsInterface.d("ComicRepository", "Configurando PagingSource para HQs")

                ComicPagingSource(
                    comicService = comicService,
                    timestamp = hashGeneratorUtilsInterface.generateTimestamp(),
                    apiKey = BuildConfig.PUBLIC_KEY,
                    hash = hashGeneratorUtilsInterface.generateHash(hashGeneratorUtilsInterface.generateTimestamp()),
                    logcatLoggerUtilsInterface = logcatLoggerUtilsInterface, // Passa o Logger para o PagingSource
                    onError = { throwable ->
                        // Lidar com o erro no repositório, se necessário
                        _errorState.postValue(throwable)
                    }
                ).also {
                    logcatLoggerUtilsInterface.d("ComicRepository", "PagingSource configurado com sucesso para HQs")
                }
            }
        ).flow.also {
            logcatLoggerUtilsInterface.d("ComicRepository", "Flow de HQs configurado com sucesso")
        }
    }
}
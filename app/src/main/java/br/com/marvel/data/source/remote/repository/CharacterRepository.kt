package br.com.marvel.data.source.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.marvel.BuildConfig
import br.com.marvel.data.model.CharacterModel
import br.com.marvel.data.source.paging.CharacterPagingSource
import br.com.marvel.data.source.remote.api.CharacterService
import br.com.marvel.domain.repository.CharacterRepositoryInterface
import br.com.marvel.utils.hashgenerator.HashGeneratorUtilsInterface
import br.com.marvel.utils.logging.LogcatLoggerUtilsInterface
import kotlinx.coroutines.flow.Flow

class CharacterRepository(
    private val characterService: CharacterService, // Serviço responsável pelas chamadas de API relacionadas a personagens
    private val hashGeneratorUtilsInterface: HashGeneratorUtilsInterface, // Utilitário para gerar hash e timestamp para autenticação na API
    private val logcatLoggerUtilsInterface: LogcatLoggerUtilsInterface // Logger para logs
) : CharacterRepositoryInterface {

    // MutableLiveData que mantém o estado dos erros que ocorrem durante a operação de carregamento
    private val _errorState = MutableLiveData<Throwable>()
    private val errorState: LiveData<Throwable> get() = _errorState

    // Retorna um LiveData que emite erros que ocorrem durante a recuperação dos dados
    override fun erro(): LiveData<Throwable> {
        return errorState
    }

    // Função que retorna um Flow com dados paginados de personagens com base no ID da HQ
    // Utiliza a biblioteca Paging para carregar os dados de maneira eficiente
    override fun getCharacterFlow(comicId: Int): Flow<PagingData<CharacterModel>> {
        logcatLoggerUtilsInterface.d("CharacterRepository", "Iniciando fluxo de personagens para HQ com ID: $comicId")

        return Pager(
            config = PagingConfig(
                pageSize = 15, // Tamanho de cada página (quantidade de itens por página)
                initialLoadSize = 15, // Quantidade de itens a serem carregados inicialmente
                prefetchDistance = 5, // Distância em relação ao fim da lista para carregar mais dados
                enablePlaceholders = false // Desativa placeholders (itens vazios durante o carregamento)
            ),
            pagingSourceFactory = {
                logcatLoggerUtilsInterface.d("CharacterRepository", "Configurando PagingSource para HQ com ID: $comicId")

                // Cria e retorna uma instância de CharacterPagingSource, que busca os personagens da API
                CharacterPagingSource(
                    comicId = comicId, // ID da HQ para obter os personagens
                    characterService = characterService, // Serviço responsável pela chamada da API de personagens
                    timestamp = hashGeneratorUtilsInterface.generateTimestamp(), // Gera o timestamp atual para a chamada da API
                    apiKey = BuildConfig.PUBLIC_KEY, // Chave pública para autenticação na API
                    hash = hashGeneratorUtilsInterface.generateHash(hashGeneratorUtilsInterface.generateTimestamp()), // Gera o hash de autenticação com base no timestamp
                    logcatLoggerUtilsInterface = logcatLoggerUtilsInterface, // Passa o Logger para o PagingSource
                    onError = { throwable ->
                        // Atualiza o estado de erro e registra o erro no log
                        _errorState.postValue(throwable)
                        logcatLoggerUtilsInterface.e("CharacterRepository", "Erro ao carregar personagens: ${throwable.message}", throwable)
                    }
                ).also {
                    logcatLoggerUtilsInterface.d("CharacterRepository", "PagingSource configurado com sucesso para HQ com ID: $comicId")
                }
            }
        ).flow.also {
            logcatLoggerUtilsInterface.d("CharacterRepository", "Flow de personagens configurado com sucesso para HQ com ID: $comicId")
        }
    }
}
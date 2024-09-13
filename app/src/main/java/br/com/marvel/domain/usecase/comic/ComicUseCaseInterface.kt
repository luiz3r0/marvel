package br.com.marvel.domain.usecase.comic

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import br.com.marvel.data.model.ComicModel
import kotlinx.coroutines.flow.Flow

interface ComicUseCaseInterface {

    // Obtém um fluxo paginado de HQs (comics).
    // Este método deve ser implementado para retornar um fluxo (Flow) que fornece dados de HQs em formato paginado.
    // A paginação permite carregar grandes listas de HQs de maneira eficiente, carregando dados conforme necessário.
    fun getComicsFlow(): Flow<PagingData<ComicModel>>

    // Obtém um LiveData que emite erros ocorridos durante a recuperação de dados.
    // Este método deve ser implementado para retornar um LiveData que pode ser observado para lidar com erros relacionados
    // à recuperação de dados de HQs, possibilitando que a UI reaja a falhas na operação de carregamento de dados.
    fun erro(): LiveData<Throwable>
}
package br.com.marvel.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import br.com.marvel.data.model.ComicModel
import kotlinx.coroutines.flow.Flow

interface ComicRepositoryInterface {

    // Retorna um fluxo de dados paginados (PagingData) de quadrinhos.
    // Esse método deve ser implementado para fornecer um fluxo (Flow) que emite dados de quadrinhos em formato paginado.
    // A paginação permite carregar grandes listas de quadrinhos de maneira eficiente, dividindo os dados em partes menores
    // que são carregadas conforme necessário.
    fun getComicsFlow(): Flow<PagingData<ComicModel>>

    // Retorna um LiveData que emite erros ocorridos durante a recuperação dos dados.
    // Este método deve ser implementado para fornecer um LiveData que pode ser observado para tratar erros relacionados
    // à recuperação de dados de quadrinhos. Isso permite que a UI reaja a falhas na operação de carregamento e forneça
    // feedback adequado ao usuário.
    fun erro(): LiveData<Throwable>
}
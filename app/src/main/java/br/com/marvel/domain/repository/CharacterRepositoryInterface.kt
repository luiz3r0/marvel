package br.com.marvel.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import br.com.marvel.data.model.CharacterModel
import kotlinx.coroutines.flow.Flow

interface CharacterRepositoryInterface {

    // Retorna um fluxo de dados paginados (PagingData) de personagens, filtrado pelo ID da HQ (comicId).
    // Esse método deve ser implementado para fornecer um fluxo (Flow) que emite dados de personagens em formato paginado.
    // A paginação é usada para carregar grandes listas de personagens de maneira eficiente, carregando os dados em partes.
    fun getCharacterFlow(comicId: Int): Flow<PagingData<CharacterModel>>

    // Retorna um LiveData que emite erros ocorridos durante a recuperação dos dados.
    // Este método deve ser implementado para fornecer um LiveData que pode ser observado para tratar erros relacionados
    // à recuperação de dados de personagens. Isso permite que a UI reaja a falhas na operação de carregamento.
    fun erro(): LiveData<Throwable>
}
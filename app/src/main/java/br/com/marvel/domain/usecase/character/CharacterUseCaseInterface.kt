package br.com.marvel.domain.usecase.character

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import br.com.marvel.data.model.CharacterModel
import kotlinx.coroutines.flow.Flow

interface CharacterUseCaseInterface {

    // Obtém um fluxo de dados paginados de personagens associados a um comic específico.
    // Este método retorna um Flow que fornece PagingData de CharacterModel para o comic com o ID especificado.
    // A paginação é usada para carregar dados em partes, otimizando o desempenho e a experiência do usuário.
    //
    // @param comicId O ID do comic para o qual os personagens serão recuperados.
    // @return Um Flow de PagingData contendo os dados dos personagens do comic especificado.
    fun getCharacterFlow(comicId: Int): Flow<PagingData<CharacterModel>>

    // Retorna um LiveData de Throwable que representa erros ocorridos durante a recuperação dos dados.
    // Isso permite que a camada de apresentação observe e trate erros, garantindo uma melhor experiência do usuário.
    fun erro(): LiveData<Throwable>
}
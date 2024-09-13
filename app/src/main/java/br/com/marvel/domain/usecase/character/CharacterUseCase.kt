package br.com.marvel.domain.usecase.character

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import br.com.marvel.data.model.CharacterModel
import br.com.marvel.domain.repository.CharacterRepositoryInterface
import kotlinx.coroutines.flow.Flow

class CharacterUseCase(private val characterRepositoryInterface: CharacterRepositoryInterface) :
    CharacterUseCaseInterface {

    // Obtém um fluxo de dados paginados de personagens com base no ID do comic.
    // Utiliza o repositório para recuperar os dados necessários e retornar como um Flow.
    override fun getCharacterFlow(comicId: Int): Flow<PagingData<CharacterModel>> {
        // Log para depuração: imprime o ID do comic que está sendo utilizado para obter os personagens.
        Log.d("CharacterUseCase", "Obtendo fluxo de personagens para o comic ID: $comicId")

        // Chama o método do repositório para obter o fluxo de dados paginados.
        return characterRepositoryInterface.getCharacterFlow(comicId)
    }

    // Retorna o LiveData de erros do repositório.
    // Isso permite que a camada de apresentação observe e trate erros.
    override fun erro(): LiveData<Throwable> {
        return characterRepositoryInterface.erro()
    }
}
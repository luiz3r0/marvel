package br.com.marvel.presentation.character

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.marvel.data.model.CharacterModel
import br.com.marvel.domain.usecase.character.CharacterUseCaseInterface
import br.com.marvel.utils.internetavailability.InternetAvailabilityUtilsInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val characterUseCaseInterface: CharacterUseCaseInterface,
    private val internetAvailabilityUtilsInterface: InternetAvailabilityUtilsInterface
) : ViewModel() {

    // Fluxo mutável para armazenar e emitir o PagingData de personagens.
    private val _characterFlow = MutableStateFlow<PagingData<CharacterModel>>(PagingData.empty())
    val characterFlow: StateFlow<PagingData<CharacterModel>> = _characterFlow.asStateFlow()

    // LiveData para indicar erro de internet.
    private val _internetErrorLiveData = MutableLiveData<Boolean>()
    val internetErrorLiveData: LiveData<Boolean> = _internetErrorLiveData

    // LiveData para indicar erro na chamada da API.
    private val _apiErrorLiveData = MutableLiveData<Boolean>()
    val apiErrorLiveData: LiveData<Boolean> = _apiErrorLiveData

    // LiveData para erros de API que emite Throwable
    private val _errorState = characterUseCaseInterface.erro()
    private val errorState: LiveData<Throwable> get() = _errorState


    init {
        // Observe o errorState e atualize _apiErrorLiveData de acordo
        setupErrorObserver()
    }

    // Função que observa o errorState e atualiza o _apiErrorLiveData
    private fun setupErrorObserver() {
        errorState.observeForever { throwable ->
            _apiErrorLiveData.value = throwable != null
        }
    }

    // Função que carrega os personagens a partir do UseCase, utilizando o comicId fornecido.
    fun loadCharacters(context: Context, comicId: Int) {
        viewModelScope.launch {
            // Verifica se a internet está disponível antes de fazer a chamada de API.
            if (internetAvailabilityUtilsInterface.isInternetAvailable(context)) {
                _internetErrorLiveData.value = false // Atualiza o estado de erro de internet para 'false'.
                try {
                    _apiErrorLiveData.value = false // Reseta o estado de erro da API.
                    Log.d("CharacterViewModel", "Carregando personagens para comicId: $comicId.")

                    // Obtém o fluxo paginado de personagens do UseCase.
                    characterUseCaseInterface.getCharacterFlow(comicId)
                        .cachedIn(viewModelScope)
                        .collectLatest { pagingData ->
                            _characterFlow.value = pagingData // Atualiza o fluxo de personagens.
                            Log.d("CharacterViewModel", "Personagens carregados com sucesso.")
                        }
                } catch (e: Exception) {
                    // Captura exceções e atualiza o LiveData de erro da API.
                    _apiErrorLiveData.value = true
                    Log.e("CharacterViewModel", "Erro ao carregar personagens: ${e.message}")
                }
            } else {
                // Atualiza o estado para indicar erro de internet.
                _internetErrorLiveData.value = true
                Log.w("CharacterViewModel", "Erro de conexão: Internet não disponível.")
            }
        }
    }
}
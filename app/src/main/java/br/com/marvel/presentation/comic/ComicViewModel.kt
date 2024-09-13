package br.com.marvel.presentation.comic

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import br.com.marvel.data.model.ComicModel
import br.com.marvel.domain.usecase.favoritecomics.FavoriteComicsUseCaseInterface
import br.com.marvel.domain.usecase.comic.ComicUseCaseInterface
import br.com.marvel.data.mapper.FavoriteComicMapper
import br.com.marvel.utils.internetavailability.InternetAvailabilityUtilsInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComicViewModel(
    private val comicUseCaseInterface: ComicUseCaseInterface,
    private val favoriteComicsUseCaseInterface: FavoriteComicsUseCaseInterface,
    private val internetAvailabilityUtilsInterface: InternetAvailabilityUtilsInterface
) : ViewModel() {

    // Flow que armazena dados paginados de quadrinhos
    private val _comicFlow = MutableStateFlow<PagingData<ComicModel>>(PagingData.empty())
    val comicFlow: StateFlow<PagingData<ComicModel>> get() = _comicFlow.asStateFlow()

    // LiveData que indica se há erro de internet
    private val _internetErrorLiveData = MutableLiveData<Boolean>()
    val internetErrorLiveData: LiveData<Boolean> get() = _internetErrorLiveData

    // LiveData que indica se houve erro na chamada da API
    private val _apiErrorLiveData = MutableLiveData<Boolean>()
    val apiErrorLiveData: LiveData<Boolean> get() = _apiErrorLiveData

    // LiveData para erros de API que emite Throwable
    private val _errorState = comicUseCaseInterface.erro()
    private val errorState: LiveData<Throwable> get() = _errorState

    // Inicialização do ViewModel
    init {
        setupErrorObserver()
    }

    // Função que observa o errorState e atualiza o _apiErrorLiveData
    private fun setupErrorObserver() {
        errorState.observeForever { throwable ->
            _apiErrorLiveData.value = throwable != null
        }
    }

    // Função responsável por carregar quadrinhos, verificando antes se há internet disponível
    fun loadComics(context: Context) {
        if (internetAvailabilityUtilsInterface.isInternetAvailable(context)) {
            // Se a internet estiver disponível, carrega quadrinhos da API
            _internetErrorLiveData.value = false
            Log.d("ComicViewModel", "Internet disponível, carregando quadrinhos da API.")
            loadComicsFromApi()
        } else {
            // Se não houver internet, sinaliza erro via LiveData
            _internetErrorLiveData.value = true
            Log.d("ComicViewModel", "Sem conexão com a internet.")
        }
    }

    // Função que carrega quadrinhos da API e atualiza o flow com dados paginados
    private fun loadComicsFromApi() {
        viewModelScope.launch {
            try {
                // Define o LiveData de erro da API como falso antes de iniciar o carregamento
                _apiErrorLiveData.value = false
                Log.d("ComicViewModel", "Carregando quadrinhos da API.")

                // Obtém IDs dos quadrinhos favoritos no banco de dados
                val favoriteComicIds = withContext(Dispatchers.IO) {
                    favoriteComicsUseCaseInterface.getAllFavoriteComicIds()
                }
                Log.d("ComicViewModel", "IDs de quadrinhos favoritos carregados: $favoriteComicIds")

                // Coleta quadrinhos da API e marca favoritos
                comicUseCaseInterface.getComicsFlow()
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        val updatedPagingData = pagingData.map { comic ->
                            comic.copy(favorite = favoriteComicIds.contains(comic.id))
                        }
                        _comicFlow.value = updatedPagingData
                        Log.d("ComicViewModel", "Quadrinhos carregados e favoritos atualizados.")
                    }
            } catch (e: Exception) {
                // Caso haja erro, sinaliza no LiveData de erro de API
                _apiErrorLiveData.value = true
                Log.e("ComicViewModel", "Erro ao carregar quadrinhos da API", e)
            }
        }
    }

    // Função chamada quando o usuário clica para favoritar ou desfavoritar um quadrinho
    fun onFavoriteClicked(comicModel: ComicModel) {
        viewModelScope.launch {
            val isFavorite = withContext(Dispatchers.IO) {
                favoriteComicsUseCaseInterface.doesComicExist(comicModel.id)
            }

            if (isFavorite) {
                // Remove o quadrinho dos favoritos se ele já estiver lá
                deleteFavoriteComicRoom(comicModel.id)
                Log.d("ComicViewModel", "Quadrinho removido dos favoritos: ${comicModel.id}")
            } else {
                // Adiciona o quadrinho aos favoritos se ele não estiver
                insertFavoriteComicRoom(comicModel)
                Log.d("ComicViewModel", "Quadrinho adicionado aos favoritos: ${comicModel.id}")
            }

            // Atualiza o status de favorito no flow de quadrinhos
            updateFavoriteStatus(comicModel.id, !isFavorite)
            Log.d(
                "ComicViewModel",
                "Status de favorito atualizado para: ${!isFavorite} para o quadrinho ${comicModel.id}"
            )
        }
    }

    // Função para atualizar o status de favorito de um quadrinho no flow
    private fun updateFavoriteStatus(comicId: Int?, isFavorite: Boolean) {
        viewModelScope.launch {
            _comicFlow.value = _comicFlow.value.map { comic ->
                if (comic.id == comicId) {
                    comic.copy(favorite = isFavorite)
                } else {
                    comic
                }
            }
            Log.d("ComicViewModel", "Flow de quadrinhos atualizado com o novo status de favorito.")
        }
    }

    // Insere o quadrinho nos favoritos no banco de dados
    private fun insertFavoriteComicRoom(comicModel: ComicModel) {
        viewModelScope.launch {
            val comicEntity = FavoriteComicMapper.comicModelToEntity(comicModel)
            favoriteComicsUseCaseInterface.insertFavoriteComicsRoom(comicEntity)
            Log.d("ComicViewModel", "Quadrinho ${comicModel.id} inserido nos favoritos.")
        }
    }

    // Remove o quadrinho dos favoritos no banco de dados
    private fun deleteFavoriteComicRoom(comicId: Int?) {
        viewModelScope.launch {
            favoriteComicsUseCaseInterface.deleteFavoriteComicsRoom(comicId)
            Log.d("ComicViewModel", "Quadrinho $comicId removido dos favoritos.")
        }
    }
}
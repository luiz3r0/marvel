package br.com.marvel.presentation.favoritecomics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import br.com.marvel.data.source.local.db.entities.FavoriteComicsEntity
import br.com.marvel.domain.usecase.favoritecomics.FavoriteComicsUseCaseInterface

class FavoriteComicsViewModel(
    private val favoriteComicsUseCaseInterface: FavoriteComicsUseCaseInterface
) : ViewModel() {

    // LiveData que representa a lista de quadrinhos favoritos
    private val _dataRoomListFavoriteComics =
        favoriteComicsUseCaseInterface.listFavoriteComicsRoom()
            .asLiveData() as MutableLiveData<List<FavoriteComicsEntity>>?
    val dataRoomListFavoriteComics: LiveData<List<FavoriteComicsEntity>>?
        get() = _dataRoomListFavoriteComics
}
package br.com.marvel.presentation.favoritecomics

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.marvel.R
import br.com.marvel.databinding.FragmentFavoriteComicsBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteComicsFragment : Fragment() {

    private val favoriteComicsViewModel: FavoriteComicsViewModel by viewModel()
    private lateinit var fragmentFavoriteComicsBinding: FragmentFavoriteComicsBinding
    private val favoriteComicsAdapter: FavoriteComicsAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla o layout do fragmento e configura a vinculação de dados
        fragmentFavoriteComicsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_comics, container, false)
        setupDataBinding()
        startRecyclerView()
        startObserveListFavoriteComics()
        startButtonBack()
        return fragmentFavoriteComicsBinding.root
    }

    // Configura a vinculação de dados do fragmento com o ViewModel
    private fun setupDataBinding() {
        fragmentFavoriteComicsBinding.favoriteComicsViewModel = favoriteComicsViewModel
        fragmentFavoriteComicsBinding.lifecycleOwner = viewLifecycleOwner
    }

    // Configura o RecyclerView para exibir a lista de quadrinhos favoritos
    private fun startRecyclerView() {
        fragmentFavoriteComicsBinding.favoriteComicsRecyclerview.setHasFixedSize(true)
        fragmentFavoriteComicsBinding.favoriteComicsRecyclerview.layoutManager =
            LinearLayoutManager(requireContext())
        fragmentFavoriteComicsBinding.favoriteComicsRecyclerview.itemAnimator = null
        fragmentFavoriteComicsBinding.favoriteComicsRecyclerview.adapter = favoriteComicsAdapter
    }

    // Observa a lista de quadrinhos favoritos no ViewModel e atualiza o adaptador
    private fun startObserveListFavoriteComics() {
        favoriteComicsViewModel.dataRoomListFavoriteComics?.observe(
            viewLifecycleOwner
        ) { favoriteComics ->
            favoriteComics?.let {
                favoriteComicsAdapter.submitList(it)
                Log.d("FavoriteComicsFragment", "Favorite comics list updated: ${it.size} items")
            }
        }
    }

    // Configura o botão "Voltar" para navegar de volta na pilha de navegação
    private fun startButtonBack() {
        fragmentFavoriteComicsBinding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
            Log.d("FavoriteComicsFragment", "Back button clicked, navigating back")
        }
    }
}
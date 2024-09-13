package br.com.marvel.presentation.comic

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.marvel.R
import br.com.marvel.data.model.ComicModel
import br.com.marvel.databinding.FragmentComicBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ComicFragment : Fragment() {

    // Injetando o ViewModel responsável pelos dados dos quadrinhos com Koin.
    private val comicViewModel: ComicViewModel by viewModel()

    // Adapter personalizado para exibir a lista de quadrinhos.
    private val comicAdapter: ComicAdapter by lazy {
        getKoin().get {
            parametersOf(
                onCharacterItemClick, // Callback para clique em personagem
                onFavoriteItemClick   // Callback para clique em favorito
            )
        }
    }

    // Binding da view do fragmento.
    private lateinit var fragmentComicBinding: FragmentComicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla o layout do fragmento e inicializa o binding.
        fragmentComicBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_comic, container, false)

        // Configura o DataBinding.
        setupDataBinding()

        // Inicializa o RecyclerView.
        startRecyclerView()

        // Observa a lista de quadrinhos e atualiza o adapter quando necessário.
        startObserveListComic()

        // Configura o clique no botão de favoritos.
        onFavoriteComicsButtonClick()

        // Observa os erros de conexão e API.
        observeErrors()

        // Retorna a raiz do binding, que contém a view do fragmento.
        return fragmentComicBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Carrega os quadrinhos ao abrir o fragmento.
        startLoadComic()
    }


    // Configura o DataBinding para associar o ViewModel à view e vincular o ciclo de vida.
    private fun setupDataBinding() {
        fragmentComicBinding.comicViewModel = comicViewModel
        fragmentComicBinding.lifecycleOwner = viewLifecycleOwner
        Log.d("ComicFragment", "Data binding configurado com ViewModel e ciclo de vida.")
    }

    // Inicializa o RecyclerView, configurando o layout e o adapter.
    private fun startRecyclerView() {
        fragmentComicBinding.comicRecyclerview.setHasFixedSize(true)
        fragmentComicBinding.comicRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        fragmentComicBinding.comicRecyclerview.itemAnimator = null // Melhorando a performance.
        fragmentComicBinding.comicRecyclerview.adapter = comicAdapter
        Log.d("ComicFragment", "RecyclerView configurado com LayoutManager e Adapter.")
    }

    // Carrega a lista de quadrinhos ao abrir o fragmento.
    private fun startLoadComic() {
        comicViewModel.loadComics(requireContext())
        Log.d("ComicFragment", "Iniciando carregamento dos quadrinhos.")
    }

    // Observa o fluxo de quadrinhos e atualiza o adapter conforme os dados chegam.
    private fun startObserveListComic() {
        viewLifecycleOwner.lifecycleScope.launch {
            comicViewModel.comicFlow.collectLatest {
                comicAdapter.submitData(it)
                Log.d("ComicFragment", "Lista de quadrinhos atualizada no adapter.")
            }
        }
    }

    // Navega para o fragmento de personagens com o ID do quadrinho clicado.
    private fun navigateToCharacterFragment(comicId: Int) {
        val bundle = Bundle().apply { putInt("comicId", comicId) }
        findNavController().navigate(
            R.id.action_comicFragment_to_marvelComicFragment,
            bundle
        )
        Log.d("ComicFragment", "Navegando para CharacterFragment com o ID do quadrinho: $comicId.")
    }

    // Configura o botão de favoritos para navegar para a tela de favoritos.
    private fun onFavoriteComicsButtonClick() {
        fragmentComicBinding.fabFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_comicFragment_to_favoriteComicsFragment)
            Log.d("ComicFragment", "Navegando para o fragmento de quadrinhos favoritos.")
        }
    }

    // Callback acionado ao clicar em um quadrinho para navegar ao fragmento de personagens.
    private val onCharacterItemClick: (ComicModel) -> Unit = { comicModel ->
        comicModel.id?.let {
            navigateToCharacterFragment(it)
            Log.d("clcick", "Quadrinho clicado: ${comicModel.id}.")
        }
    }

    // Callback acionado ao clicar no botão de favoritos de um quadrinho.
    private val onFavoriteItemClick: (ComicModel) -> Unit = { comicModel ->
        comicViewModel.onFavoriteClicked(comicModel)
        Log.d("clcick", "Favorito clicado: ${comicModel.id}.")
    }

    // Observa erros de conexão e de API.
    private fun observeErrors() {
        comicViewModel.internetErrorLiveData.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                showNoInternet()
            }
        }

        comicViewModel.apiErrorLiveData.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                showErrorApi()
            }
        }
    }

    // Exibe Snackbar para erros de API, permitindo ao usuário tentar novamente.
    private fun showErrorApi() {
        val snackbar = Snackbar.make(
            requireView(),
            "Erro ao tentar baixar os quadrinhos, tente novamente.",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Atualizar") {
                Log.d(
                    "ComicFragment",
                    "Botão 'Atualizar' clicado para tentar recarregar os quadrinhos."
                )
                comicViewModel.loadComics(requireContext())
            }
            .setActionTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.snackbaractiontextcolor
                )
            )

        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        snackbar.show()
        Log.d("ComicFragment", "Snackbar exibido: Erro ao baixar quadrinhos.")
    }

    // Exibe Snackbar para erros de conexão de internet, permitindo ao usuário tentar novamente.
    private fun showNoInternet() {
        val snackbar = Snackbar.make(
            requireView(),
            "Verifique sua internet e tente novamente.",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Atualizar") {
                Log.d("ComicFragment", "Botão 'Atualizar' clicado após erro de conexão.")
                comicViewModel.loadComics(requireContext()) // Tenta novamente o carregamento
            }
            .setActionTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.snackbaractiontextcolor
                )
            )

        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        snackbar.show()
        Log.d("ComicFragment", "Snackbar exibido: Verifique sua internet.")
    }
}
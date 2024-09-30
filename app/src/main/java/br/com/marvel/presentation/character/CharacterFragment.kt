package br.com.marvel.presentation.character

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
import br.com.marvel.databinding.FragmentCharacterBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterFragment : Fragment() {

    // Inicializa o ViewModel associado ao fragmento, passando o comicId recebido nos argumentos.
    private val characterViewModel: CharacterViewModel by viewModel()

    // Binding do layout para o fragmento, usando DataBinding.
    private lateinit var fragmentCharacterBinding: FragmentCharacterBinding

    // Adapter utilizado para exibir a lista de personagens no RecyclerView.
    private val characterAdapter: CharacterAdapter by inject()

    // Infla o layout do fragmento e faz as configurações iniciais.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla o layout do fragmento usando DataBinding.
        fragmentCharacterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_character, container, false)

        // Configura o DataBinding para associar o ViewModel ao layout.
        setupDataBinding()

        // Configura o RecyclerView para exibir a lista de personagens.
        startRecyclerView()

        // Observa as atualizações na lista de personagens e atualiza o RecyclerView.
        startObserveListCharacter()

        // Configura o botão de voltar.
        startButtonBack()

        // Observa possíveis erros (internet e API) e os trata.
        observeErrors()

        // Retorna a raiz do layout inflado.
        return fragmentCharacterBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicia o carregamento dos personagens ao abrir o fragmento.
        startLoadCharacters()
    }

    // Configura o DataBinding associando o ViewModel e o ciclo de vida do fragmento.
    private fun setupDataBinding() {
        fragmentCharacterBinding.marvelComicViewModel = characterViewModel
        fragmentCharacterBinding.lifecycleOwner = viewLifecycleOwner
        Log.d("CharacterFragment", "DataBinding configurado.")
    }

    // Inicializa o RecyclerView com layout, adapter e configurações necessárias.
    private fun startRecyclerView() {
        fragmentCharacterBinding.characterRecyclerview.setHasFixedSize(true)
        fragmentCharacterBinding.characterRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        fragmentCharacterBinding.characterRecyclerview.itemAnimator = null // Desativa animações para melhorar desempenho.
        fragmentCharacterBinding.characterRecyclerview.adapter = characterAdapter
        Log.d("CharacterFragment", "RecyclerView configurado.")
    }

    // Observa o fluxo de dados do ViewModel e atualiza o adapter do RecyclerView.
    private fun startObserveListCharacter() {
        lifecycleScope.launch {
            characterViewModel.characterFlow.collectLatest(characterAdapter::submitData)
            Log.d("CharacterFragment", "Observando lista de personagens.")
        }
    }

    // Configura o botão de voltar para navegar ao fragmento anterior.
    private fun startButtonBack() {
        fragmentCharacterBinding.buttonBack.setOnClickListener {
            findNavController().popBackStack() // Navega de volta na pilha de navegação.
            Log.d("CharacterFragment", "Botão de voltar pressionado.")
        }
    }

    // Inicia o carregamento da lista de personagens ao abrir o fragmento.
    private fun startLoadCharacters() {
        characterViewModel.loadCharacters(requireContext(), requireArguments().getInt("comicId"))
        Log.d("CharacterFragment", "Carregamento dos personagens iniciado.")
    }

    // Observa os LiveData de erro de internet e API para exibir mensagens ao usuário.
    private fun observeErrors() {
        characterViewModel.internetErrorLiveData.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                showNoInternet()
                Log.d("CharacterFragment", "Erro de conexão detectado.")
            }
        }

        characterViewModel.apiErrorLiveData.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                showErrorApi()
                Log.d("CharacterFragment", "Erro de API detectado.")
            }
        }
    }

    // Exibe uma mensagem Snackbar para erro de API, permitindo ao usuário tentar novamente.
    private fun showErrorApi() {
        val snackbar = Snackbar.make(
            requireView(),
            "Erro ao tentar baixar os quadrinhos, tente novamente.",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Atualizar") {
            Log.d("CharacterFragment", "Tentativa de atualizar após erro de API.")
            characterViewModel.loadCharacters(requireContext(), requireArguments().getInt("comicId")) // Tenta recarregar os personagens.
        }.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.snackbaractiontextcolor))

        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        snackbar.show()
        Log.d("CharacterFragment", "Snackbar exibido para erro de API.")
    }

    // Exibe uma mensagem Snackbar para erro de internet, permitindo ao usuário tentar novamente.
    private fun showNoInternet() {
        val snackbar = Snackbar.make(
            requireView(),
            "Verifique sua internet e tente novamente.",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Atualizar") {
            Log.d("CharacterFragment", "Tentativa de atualizar após erro de conexão.")
            characterViewModel.loadCharacters(requireContext(), requireArguments().getInt("comicId")) // Tenta novamente o carregamento.
        }.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.snackbaractiontextcolor))

        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        snackbar.show()
        Log.d("CharacterFragment", "Snackbar exibido para erro de internet.")
    }
}
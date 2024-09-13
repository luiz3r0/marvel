package br.com.marvel.domain.usecase.comic

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import br.com.marvel.data.model.ComicModel
import br.com.marvel.domain.repository.ComicRepositoryInterface
import kotlinx.coroutines.flow.Flow

class ComicUseCase(
    private val comicRepositoryInterface: ComicRepositoryInterface // Interface do repositório para acessar dados de HQs
) : ComicUseCaseInterface {

    // Obtém um fluxo paginado de HQs (comics).
    // Este método chama o método correspondente no repositório para recuperar a lista de HQs de forma paginada.
    // O fluxo retornado pode ser observado para atualizar a UI com os dados carregados.
    override fun getComicsFlow(): Flow<PagingData<ComicModel>> {
        return comicRepositoryInterface.getComicsFlow()
    }

    // Retorna um LiveData que emite erros ocorridos durante a recuperação de dados de HQs.
    // Esse LiveData pode ser observado para lidar com erros na UI, por exemplo, exibindo mensagens de erro ao usuário.
    override fun erro(): LiveData<Throwable> {
        return comicRepositoryInterface.erro()
    }
}
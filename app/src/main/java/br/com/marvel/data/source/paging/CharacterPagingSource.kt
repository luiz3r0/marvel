package br.com.marvel.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.marvel.data.model.CharacterModel
import br.com.marvel.data.source.remote.api.CharacterService
import br.com.marvel.utils.logging.LogcatLoggerUtilsInterface
import retrofit2.HttpException
import java.io.IOException

class CharacterPagingSource(
    private val characterService: CharacterService,
    private val comicId: Int,
    private val timestamp: String,
    private val apiKey: String,
    private val hash: String,
    private val logcatLoggerUtilsInterface: LogcatLoggerUtilsInterface,
    private val onError: (Throwable) -> Unit // Adicionado para o tratamento de erros
) : PagingSource<Int, CharacterModel>() {

    companion object {
        private const val TAG = "CharacterPagingSource"
        private const val PAGE_SIZE = 15
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterModel> {
        val page = params.key ?: 0
        val offset = page * PAGE_SIZE

        logcatLoggerUtilsInterface.d(TAG, "Loading page: $page with offset: $offset")

        return try {
            // Fazendo a requisição para a API
            val response = characterService.getCharacters(
                comicId = comicId,
                timestamp = timestamp,
                apiKey = apiKey,
                hash = hash,
                offset = offset,
                limit = PAGE_SIZE
            )

            // Verificação básica da resposta
            if (response.data?.results == null) {
                logcatLoggerUtilsInterface.e(TAG, "Response data or results is null")
                onError(Exception("Response data is null")) // Notifica o erro
                return LoadResult.Error(Exception("Response data is null"))
            }

            // Mapeia o resultado para a lista de personagens
            val characters = response.data.results

            logcatLoggerUtilsInterface.d(TAG, "Number of characters received: ${characters.size}")

            // Retorna a página carregada
            LoadResult.Page(
                data = characters,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (characters.size < PAGE_SIZE) null else page + 1
            )

        } catch (e: IOException) {
            // Log para erros de IO
            logcatLoggerUtilsInterface.e(TAG, "Network error: ${e.localizedMessage}", e)
            onError(e) // Notifica o erro
            LoadResult.Error(e)
        } catch (e: HttpException) {
            // Log para erros HTTP inesperados
            logcatLoggerUtilsInterface.e(TAG, "HTTP exception: ${e.localizedMessage}", e)
            onError(e) // Notifica o erro
            LoadResult.Error(e)
        } catch (e: Exception) {
            // Log para quaisquer outros erros inesperados
            logcatLoggerUtilsInterface.e(TAG, "Unexpected error: ${e.localizedMessage}", e)
            onError(e) // Notifica o erro
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterModel>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}
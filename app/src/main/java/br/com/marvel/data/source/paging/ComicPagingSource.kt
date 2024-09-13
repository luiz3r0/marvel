package br.com.marvel.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.marvel.data.source.remote.api.ComicService
import br.com.marvel.data.model.ComicModel
import br.com.marvel.utils.logging.LogcatLoggerUtilsInterface
import retrofit2.HttpException
import java.io.IOException

class ComicPagingSource(
    private val comicService: ComicService,
    private val timestamp: String,
    private val apiKey: String,
    private val hash: String,
    private val logcatLoggerUtilsInterface: LogcatLoggerUtilsInterface,
    private val onError: (Throwable) -> Unit
) : PagingSource<Int, ComicModel>() {

    companion object {
        private const val TAG = "ComicPagingSource"
        private const val PAGE_SIZE = 15
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicModel> {
        val page = params.key ?: 0
        val offset = page * PAGE_SIZE

        logcatLoggerUtilsInterface.d(TAG, "Loading page: $page with offset: $offset")

        return try {
            // Fazendo a requisição para a API
            val response = comicService.getComics(
                timestamp = timestamp,
                apiKey = apiKey,
                hash = hash,
                offset = offset,
                limit = PAGE_SIZE
            )

            // Verificação básica da resposta
            if (response.data?.results == null) {
                logcatLoggerUtilsInterface.e(TAG, "Response data or results is null")
                return LoadResult.Error(Exception("Response data is null"))
            }

            // Mapeia o resultado para a lista de HQs
            val comics = response.data.results.map { it.copy(favorite = false) }

            logcatLoggerUtilsInterface.d(TAG, "Number of comics received: ${comics.size}")

            // Retorna a página carregada
            LoadResult.Page(
                data = comics,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (comics.size < PAGE_SIZE) null else page + 1
            )

        } catch (e: IOException) {
            // Log para erros de IO
            logcatLoggerUtilsInterface.e(TAG, "Network error: ${e.localizedMessage}", e)
            onError(e)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            // Log para erros HTTP inesperados
            logcatLoggerUtilsInterface.e(TAG, "HTTP exception: ${e.localizedMessage}", e)
            onError(e)
            LoadResult.Error(e)
        } catch (e: Exception) {
            // Log para quaisquer outros erros inesperados
            logcatLoggerUtilsInterface.e(TAG, "Unexpected error: ${e.localizedMessage}", e)
            onError(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ComicModel>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}
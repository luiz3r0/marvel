package br.com.marvel.data.paging

import androidx.paging.PagingSource
import br.com.marvel.data.model.*
import br.com.marvel.data.source.paging.ComicPagingSource
import br.com.marvel.data.source.remote.api.ComicService
import br.com.marvel.utils.logging.LogcatLoggerUtilsInterface
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertNull
import retrofit2.HttpException
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ComicPagingSourceTest {

    private lateinit var comicService: ComicService
    private lateinit var logcatLoggerUtilsInterface: LogcatLoggerUtilsInterface
    private lateinit var pagingSource: ComicPagingSource

    @Before
    fun setup() {
        // Inicializa os mocks e o PagingSource antes de cada teste
        comicService = mock(ComicService::class.java)
        logcatLoggerUtilsInterface = mock(LogcatLoggerUtilsInterface::class.java)
        pagingSource = ComicPagingSource(
            comicService = comicService,
            timestamp = "timestamp",
            apiKey = "apiKey",
            hash = "hash",
            logcatLoggerUtilsInterface = logcatLoggerUtilsInterface,
            onError = {}
        )
    }

    @Test
    fun loadReturnsCorrectLoadResultPageWhenSuccessful() = runBlocking {
        // Mock da resposta bem-sucedida
        val mockResponse = ComicResponseModel(
            code = 200,
            status = "Ok",
            copyright = "© Marvel",
            attributionText = "Data provided by Marvel.",
            attributionHTML = "<a href='http://marvel.com'>Data provided by Marvel.</a>",
            data = ComicData(
                offset = 0,
                limit = 15,
                total = 30,
                count = 15,
                results = listOf(
                    ComicModel(
                        id = 1,
                        digitalId = 12345,
                        title = "Comic 1",
                        issueNumber = 1.0,
                        variantDescription = "Variant A",
                        description = "A description of Comic 1",
                        modified = "2023-09-01T12:00:00Z",
                        isbn = "978-1-23-456789-0",
                        upc = "123456789012",
                        diamondCode = "D12345",
                        ean = "1234567890123",
                        issn = "1234-5678",
                        format = "Comic",
                        pageCount = 32,
                        textObjects = listOf(
                            TextObject(
                                type = "issue",
                                language = "en",
                                text = "Text about the issue"
                            )
                        ),
                        resourceURI = "http://marvel.com/comics/1",
                        urls = listOf(
                            Url(
                                type = "detail",
                                url = "http://marvel.com/comics/1"
                            )
                        ),
                        series = Series(
                            resourceURI = "http://marvel.com/series/1",
                            name = "Series 1"
                        ),
                        variants = listOf(
                            Variant(
                                resourceURI = "http://marvel.com/variants/1",
                                name = "Variant 1"
                            )
                        ),
                        collections = listOf(
                            Collection(
                                resourceURI = "http://marvel.com/collections/1",
                                name = "Collection 1"
                            )
                        ),
                        collectedIssues = listOf(
                            CollectedIssue(
                                resourceURI = "http://marvel.com/collectedIssues/1",
                                name = "Collected Issue 1"
                            )
                        ),
                        dates = listOf(
                            Date(
                                type = "onsaleDate",
                                date = "2023-09-01T00:00:00Z"
                            )
                        ),
                        prices = listOf(
                            Price(
                                type = "printPrice",
                                price = 3.99f
                            )
                        ),
                        thumbnail = Image(
                            path = "http://marvel.com/images/thumbnail",
                            extension = "jpg"
                        ),
                        images = listOf(
                            Image(
                                path = "http://marvel.com/images/1",
                                extension = "jpg"
                            )
                        ),
                        creators = Creators(
                            available = 2,
                            returned = 2,
                            collectionURI = "http://marvel.com/creators",
                            items = listOf(
                                CreatorItem(
                                    resourceURI = "http://marvel.com/creators/1",
                                    name = "Creator 1",
                                    role = "writer"
                                )
                            )
                        ),
                        characters = Characters(
                            available = 3,
                            returned = 3,
                            collectionURI = "http://marvel.com/characters",
                            items = listOf(
                                CharacterItem(
                                    resourceURI = "http://marvel.com/characters/1",
                                    name = "Character 1",
                                    role = "hero"
                                )
                            )
                        ),
                        stories = Stories(
                            available = 1,
                            returned = 1,
                            collectionURI = "http://marvel.com/stories",
                            items = listOf(
                                StoryItem(
                                    resourceURI = "http://marvel.com/stories/1",
                                    name = "Story 1",
                                    type = "adventure"
                                )
                            )
                        ),
                        events = Events(
                            available = 1,
                            returned = 1,
                            collectionURI = "http://marvel.com/events",
                            items = listOf(
                                EventItem(
                                    resourceURI = "http://marvel.com/events/1",
                                    name = "Event 1"
                                )
                            )
                        ),
                        favorite = false
                    )
                )
            ),
            etag = "etag"
        )

        // Configura o mock para retornar a resposta simulada
        `when`(comicService.getComics("timestamp", "apiKey", "hash", 0, 15)).thenReturn(mockResponse)

        // Executa o método de carga e verifica o resultado
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(
            key = null,
            loadSize = 15,
            placeholdersEnabled = false
        ))

        // Verifica se o resultado é uma página com dados
        assertTrue(result is PagingSource.LoadResult.Page)
        assertEquals(1, result.data.size)
        assertNull(result.prevKey)

        // Verifica se a próxima chave é nula quando o número de resultados é menor que o tamanho do carregamento
        val isNextKeyNull = (mockResponse.data?.results?.size ?: 0) < 15
        assertEquals(isNextKeyNull, result.nextKey == null)
    }

    @Test
    fun loadReturnsLoadResultErrorWhenHttpErrorOccurs() = runBlocking {
        // Configura o mock para lançar uma exceção de HTTP
        val mockException = HttpException(retrofit2.Response.error<Any>(500, "".toResponseBody(null)))
        `when`(comicService.getComics("timestamp", "apiKey", "hash", 0, 15)).thenThrow(mockException)

        // Executa o método de carga e verifica o resultado
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(
            key = null,
            loadSize = 15,
            placeholdersEnabled = false
        ))

        // Verifica se o resultado é um erro
        assertTrue(result is PagingSource.LoadResult.Error)
        assertTrue(result.throwable is HttpException)
    }

    @Test
    fun loadReturnsLoadResultErrorWhenExceptionOccurs() = runBlocking {
        // Configura o mock para lançar uma exceção genérica
        `when`(comicService.getComics("timestamp", "apiKey", "hash", 0, 15))
            .thenThrow(RuntimeException())

        // Executa o método de carga e verifica o resultado
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(
            key = null,
            loadSize = 15,
            placeholdersEnabled = false
        ))

        // Verifica se o resultado é um erro
        assertTrue(result is PagingSource.LoadResult.Error)
        assertTrue(result.throwable is RuntimeException)
    }
}
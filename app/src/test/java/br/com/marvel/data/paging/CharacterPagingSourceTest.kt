package br.com.marvel.data.paging

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import br.com.marvel.data.model.CharacterData
import br.com.marvel.data.model.CharacterEventItem
import br.com.marvel.data.model.CharacterImage
import br.com.marvel.data.model.CharacterModel
import br.com.marvel.data.model.CharacterResponseModel
import br.com.marvel.data.model.CharacterStoryItem
import br.com.marvel.data.model.CharacterUrl
import br.com.marvel.data.model.ComicInfo
import br.com.marvel.data.model.ComicItem
import br.com.marvel.data.model.EventInfo
import br.com.marvel.data.model.SeriesInfo
import br.com.marvel.data.model.SeriesItem
import br.com.marvel.data.model.StoryInfo
import br.com.marvel.data.source.paging.CharacterPagingSource
import br.com.marvel.data.source.remote.api.CharacterService
import br.com.marvel.utils.logging.LogcatLoggerUtilsInterface
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.HttpException
import retrofit2.Response

class CharacterPagingSourceTest {

    // Variáveis para armazenar o serviço de personagens, a fonte de paginação e o logger
    private lateinit var characterService: CharacterService
    private lateinit var pagingSource: CharacterPagingSource
    private lateinit var logcatLoggerUtilsInterface: LogcatLoggerUtilsInterface

    @Before
    fun setup() {
        // Inicializa os mocks e o PagingSource antes de cada teste
        characterService = mock(CharacterService::class.java) // Mock do serviço de personagens
        logcatLoggerUtilsInterface = mock(LogcatLoggerUtilsInterface::class.java) // Mock do logger
        pagingSource = CharacterPagingSource(
            characterService = characterService,
            comicId = 123,
            timestamp = "timestamp",
            apiKey = "apiKey",
            hash = "hash",
            logcatLoggerUtilsInterface = logcatLoggerUtilsInterface,
            onError = {}
        ) // Inicializa a fonte de paginação
    }

    @Test
    fun loadReturnsCorrectLoadResultPageWhenSuccessful() = runBlocking {
        // Mock do retorno bem-sucedido sem Response
        val mockResponse = CharacterResponseModel(
            code = 200,
            status = "Ok",
            copyright = "© Marvel",
            attributionText = "Data provided by Marvel.",
            attributionHTML = "<a href='http://marvel.com'>Data provided by Marvel.</a>",
            data = CharacterData(
                offset = 0,
                limit = 15,
                total = 30,
                count = 15,
                results = listOf(
                    // Mock de um personagem da lista de resultados
                    CharacterModel(
                        id = 1,
                        name = "Character 1",
                        description = "Description of Character 1",
                        modified = "2024-09-10",
                        resourceURI = "http://marvel.com/characters/1",
                        urls = listOf(
                            CharacterUrl(type = "wiki", url = "http://marvel.com/wiki/Character_1")
                        ),
                        thumbnail = CharacterImage(
                            path = "http://marvel.com/images/thumbnail",
                            extension = "jpg"
                        ),
                        comics = ComicInfo(
                            available = 10,
                            returned = 10,
                            collectionURI = "http://marvel.com/comics",
                            items = listOf(
                                ComicItem(
                                    resourceURI = "http://marvel.com/comic/1",
                                    name = "Comic 1"
                                )
                            )
                        ),
                        stories = StoryInfo(
                            available = 5,
                            returned = 5,
                            collectionURI = "http://marvel.com/stories",
                            items = listOf(
                                CharacterStoryItem(
                                    resourceURI = "http://marvel.com/story/1",
                                    name = "Story 1",
                                    type = "adventure"
                                )
                            )
                        ),
                        events = EventInfo(
                            available = 2,
                            returned = 2,
                            collectionURI = "http://marvel.com/events",
                            items = listOf(
                                CharacterEventItem(
                                    resourceURI = "http://marvel.com/event/1",
                                    name = "Event 1"
                                )
                            )
                        ),
                        series = SeriesInfo(
                            available = 3,
                            returned = 3,
                            collectionURI = "http://marvel.com/series",
                            items = listOf(
                                SeriesItem(
                                    resourceURI = "http://marvel.com/series/1",
                                    name = "Series 1"
                                )
                            )
                        )
                    )
                )
            ),
            etag = "etag"
        )

        // Configura o mock para retornar a resposta simulada
        `when`(characterService.getCharacters(
            comicId = 123,
            timestamp = "timestamp",
            apiKey = "apiKey",
            hash = "hash",
            offset = 0,
            limit = 15
        )).thenReturn(mockResponse)

        // Executa o método de carga e verifica o resultado
        val result = pagingSource.load(LoadParams.Refresh(
            key = null,
            loadSize = 15,
            placeholdersEnabled = false
        ))

        // Verifica se o resultado é uma página com dados
        assertTrue(result is LoadResult.Page)
        result as LoadResult.Page // Cast para acessar as propriedades

        // Verifica o tamanho da lista de dados carregados
        assertEquals(1, result.data.size)
        // Verifica se a chave anterior (prevKey) é nula
        assertNull(result.prevKey)

        // Verifica se a próxima chave é nula quando o número de resultados é menor que o tamanho do carregamento
        val isNextKeyNull = (mockResponse.data?.results?.size ?: 0) < 15
        assertEquals(isNextKeyNull, result.nextKey == null)
    }

    @Test
    fun loadReturnsLoadResultErrorWhenHttpErrorOccurs() = runBlocking {
        // Simula um erro HTTP lançando uma exceção
        `when`(characterService.getCharacters(
            comicId = 123,
            timestamp = "timestamp",
            apiKey = "apiKey",
            hash = "hash",
            offset = 0,
            limit = 15
        )).thenThrow(HttpException(Response.error<Any>(500, "".toResponseBody(null))))

        // Executa o método de carga e verifica o resultado
        val result = pagingSource.load(LoadParams.Refresh(
            key = null,
            loadSize = 15,
            placeholdersEnabled = false
        ))

        // Verifica se o resultado é um erro
        assertTrue(result is LoadResult.Error)
        result as LoadResult.Error // Cast para acessar a propriedade throwable

        // Verifica se o erro foi causado por uma exceção HTTP
        assertTrue(result.throwable is HttpException)
    }

    @Test
    fun loadReturnsLoadResultErrorWhenUnexpectedExceptionOccurs() = runBlocking {
        // Configura o mock para lançar uma exceção inesperada (RuntimeException)
        `when`(characterService.getCharacters(
            comicId = 123,
            timestamp = "timestamp",
            apiKey = "apiKey",
            hash = "hash",
            offset = 0,
            limit = 15
        )).thenThrow(RuntimeException())

        // Executa o método de carga e verifica o resultado
        val result = pagingSource.load(LoadParams.Refresh(
            key = null,
            loadSize = 15,
            placeholdersEnabled = false
        ))

        // Verifica se o resultado é um erro
        assertTrue(result is LoadResult.Error)
        result as LoadResult.Error // Cast para acessar a propriedade throwable

        // Verifica se o erro foi causado por uma exceção RuntimeException
        assertTrue(result.throwable is RuntimeException)
    }
}
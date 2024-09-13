package br.com.marvel.data.source.remote.api

import br.com.marvel.data.model.CharacterResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterService {

    // Faz uma requisição GET para obter a lista de personagens de uma HQ específica
    @GET("/v1/public/comics/{comicId}/characters")
    suspend fun getCharacters(
        @Path("comicId") comicId: Int, // O ID da HQ cujos personagens serão buscados
        @Query("ts") timestamp: String, // Timestamp necessário para autenticação da API
        @Query("apikey") apiKey: String, // Chave de API pública para autenticação
        @Query("hash") hash: String, // Hash MD5 gerado para autenticação da Marvel API
        @Query("offset") offset: Int, // Offset para paginação dos resultados
        @Query("limit") limit: Int // Limite de resultados retornados
    ): CharacterResponseModel
}
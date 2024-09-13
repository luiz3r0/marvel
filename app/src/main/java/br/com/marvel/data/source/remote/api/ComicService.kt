package br.com.marvel.data.source.remote.api

import br.com.marvel.data.model.ComicResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ComicService {

    // Faz uma requisição GET para obter a lista de HQs
    @GET("/v1/public/comics")
    suspend fun getComics(
        @Query("ts") timestamp: String,  // Timestamp necessário para autenticação da API
        @Query("apikey") apiKey: String, // Chave de API pública para autenticação
        @Query("hash") hash: String,     // Hash MD5 gerado para autenticação na API da Marvel
        @Query("offset") offset: Int,    // Offset para paginação dos resultados
        @Query("limit") limit: Int       // Limite de resultados retornados por página
    ): ComicResponseModel
}
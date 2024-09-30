package br.com.marvel.data.source.remote.retrofit

import android.app.Application
import android.util.Log
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MarvelRetrofitClient(private val androidApplication: Application) :
    MarvelRetrofitClientInterface {

    // Função genérica que inicia o Retrofit para qualquer serviço fornecido
    override fun <T> startRetrofit(
        serviceClass: Class<T> // Tipo do serviço da API que será usado
    ): T {
        Log.d("MarvelRetrofitClient", "Iniciando Retrofit para o serviço: ${serviceClass.simpleName}")

        return Retrofit.Builder()
            .baseUrl(BASE_URL) // Define a URL base da API da Marvel
            .addConverterFactory(GsonConverterFactory.create()) // Adiciona o conversor Gson para converter JSON em objetos Kotlin
            .client(
                OkHttpClient.Builder()
                    .cache(Cache(androidApplication.cacheDir, 10 * 1024 * 1024)) // Configura cache de 10 MB
                    .readTimeout(60, TimeUnit.SECONDS) // Define tempo limite de leitura de 60 segundos
                    .writeTimeout(60, TimeUnit.SECONDS) // Define tempo limite de escrita de 60 segundos
                    .build() // Constrói o cliente OkHttp
            )
            .build()
            .create(serviceClass) // Cria a instância do serviço especificado
            .also {
                Log.d("MarvelRetrofitClient", "Retrofit criado com sucesso para o serviço: ${serviceClass.simpleName}")
            }
    }

    companion object {
        // URL base da API da Marvel
        private const val BASE_URL = "https://gateway.marvel.com/"
    }
}
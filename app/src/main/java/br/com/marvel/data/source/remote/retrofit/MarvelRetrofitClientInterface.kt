package br.com.marvel.data.source.remote.retrofit

interface MarvelRetrofitClientInterface {

    // Função genérica que inicia o Retrofit para qualquer classe de serviço fornecida
    fun <T> startRetrofit(serviceClass: Class<T>): T
}
package br.com.marvel

import android.app.Application
import android.util.Log
import br.com.marvel.di.Module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializa o Koin para gerenciar dependências.
        startKoin()
    }

    private fun startKoin() {
        startKoin {
            // Configura o contexto da aplicação para o Koin.
            androidContext(this@Application)
            // Adiciona os módulos que contêm as definições de dependências.
            modules(listOf(Module))
        }
        // Log para confirmar que o Koin foi iniciado.
        Log.d("MyApplication", "Koin iniciado com sucesso")
    }
}
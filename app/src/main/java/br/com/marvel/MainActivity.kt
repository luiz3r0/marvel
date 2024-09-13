package br.com.marvel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Inicializa a tela de splash.
        startSplashScreen()
        super.onCreate(savedInstanceState)
        // Define o layout da atividade principal.
        setContentView(R.layout.activity_main)
    }

    private fun startSplashScreen() {
        // Instala a tela de splash usando a API de splash screen.
        installSplashScreen()
    }
}
package br.com.marvel.utils.internetavailability

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log


class InternetAvailabilityUtils : InternetAvailabilityUtilsInterface {

    override fun isInternetAvailable(context: Context): Boolean {
        Log.d("InternetAvailabilityUtils", "Iniciando verificação da disponibilidade de internet")

        // Obtém o gerenciador de conectividade
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Verifica se há uma rede ativa
        val network = connectivityManager.activeNetwork ?: run {
            Log.d("InternetAvailabilityUtils", "Nenhuma rede ativa encontrada")
            return false
        }

        // Verifica as capacidades da rede ativa
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: run {
            Log.d("InternetAvailabilityUtils", "Nenhuma capacidade de rede encontrada")
            return false
        }

        // Verifica se a rede possui capacidade de internet e está validada
        val isAvailable = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        // Log do resultado da verificação
        Log.d("InternetAvailabilityUtils", "Internet disponível: $isAvailable")
        return isAvailable
    }
}
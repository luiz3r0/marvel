package br.com.marvel.utils.internetavailability

import android.content.Context

interface InternetAvailabilityUtilsInterface {

    fun isInternetAvailable(context: Context): Boolean
}
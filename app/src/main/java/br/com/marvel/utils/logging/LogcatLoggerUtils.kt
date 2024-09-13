package br.com.marvel.utils.logging

import android.util.Log

class LogcatLoggerUtils : LogcatLoggerUtilsInterface {

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}
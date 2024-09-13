package br.com.marvel.utils.logging

interface LogcatLoggerUtilsInterface {

    fun d(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable? = null)
}
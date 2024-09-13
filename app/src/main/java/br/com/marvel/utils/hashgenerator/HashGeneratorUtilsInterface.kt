package br.com.marvel.utils.hashgenerator

interface HashGeneratorUtilsInterface {

    fun generateTimestamp(): String
    fun generateHash(timestamp: String): String
}
package br.com.marvel.utils.hashgenerator

import android.util.Log
import br.com.marvel.BuildConfig
import java.security.MessageDigest

// Implementa a interface HashGeneratorUtilsInterface para fornecer funcionalidades de hash
class HashGeneratorUtils : HashGeneratorUtilsInterface {
    // Chaves públicas e privadas para gerar o hash
    private val publicKey = BuildConfig.PUBLIC_KEY
    private val privateKey = BuildConfig.PRIVATE_KEY

    // Gera um timestamp atual
    override fun generateTimestamp(): String {
        val timestamp = System.currentTimeMillis().toString()
        Log.d("HashGeneratorUtils", "Timestamp gerado: $timestamp")
        return timestamp
    }

    // Gera um hash MD5 com base no timestamp e nas chaves pública e privada
    override fun generateHash(timestamp: String): String {
        // Cria a string de entrada para o hash
        val input = "$timestamp$privateKey$publicKey"
        Log.d("HashGeneratorUtils", "Input para hash: $input")

        // Inicializa o MessageDigest com o algoritmo MD5
        val md5 = MessageDigest.getInstance("MD5")

        // Gera o digest MD5 da string de entrada
        val digest = md5.digest(input.toByteArray())

        // Converte o digest para uma representação hexadecimal
        val hash = digest.joinToString("") { String.format("%02x", it) }

        // Adiciona um log para o hash gerado
        Log.d("HashGeneratorUtils", "Hash gerado: $hash")

        return hash
    }
}
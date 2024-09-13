package br.com.marvel.databinding

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

@BindingAdapter("loadUrlImage")
fun loadUrlImage(imageView: ImageView, url: String?) {
    // Log para verificar se a URL é nula ou vazia
    Log.d("BindingAdapter", "Tentando carregar imagem. URL fornecida: $url")

    // Substitui "http://" por "https://" na URL para garantir segurança
    val httpsUrl = url?.replace("http://", "https://")

    // Log para verificar a URL após a substituição
    Log.d("BindingAdapter", "URL modificada para HTTPS: $httpsUrl")

    // Verifica se a URL não é nula ou vazia antes de tentar carregar a imagem
    if (!httpsUrl.isNullOrEmpty()) {
        // Usando Glide para carregar a imagem no ImageView
        Glide.with(imageView.context)
            .load(httpsUrl)
            .fitCenter() // Ajusta a imagem dentro do ImageView
            .thumbnail(Glide.with(imageView.context).load(httpsUrl).sizeMultiplier(0.1f)) // Carrega uma miniatura antes
            .transition(withCrossFade()) // Efeito de transição ao carregar a imagem
            .into(imageView) // Define a imagem no ImageView

        // Log de sucesso ao iniciar o carregamento da imagem
        Log.d("BindingAdapter", "Carregamento da imagem iniciado com sucesso.")
    } else {
        // Log para informar que a URL está nula ou vazia
        Log.e("BindingAdapter", "Falha ao carregar imagem: URL é nula ou vazia.")
    }
}
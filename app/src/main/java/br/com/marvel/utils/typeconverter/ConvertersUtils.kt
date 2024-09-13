package br.com.marvel.utils.typeconverter

import androidx.room.TypeConverter
import br.com.marvel.data.model.Image
import com.google.gson.Gson

class ConvertersUtils {

    @TypeConverter
    fun fromImage(image: Image?): String? {
        return Gson().toJson(image) // Converte o objeto `Image` em JSON.
    }

    @TypeConverter
    fun toImage(value: String?): Image? {
        return Gson().fromJson(value, Image::class.java) // Converte a string JSON em um objeto `Image`.
    }
}
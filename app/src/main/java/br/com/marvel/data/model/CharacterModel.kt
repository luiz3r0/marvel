package br.com.marvel.data.model

// Modelo de resposta para a API de personagens
data class CharacterResponseModel(
    val code: Int?, // Código de status HTTP da resposta
    val status: String?, // Status da resposta (ex.: "Ok", "Error")
    val copyright: String?, // Informações sobre direitos autorais
    val attributionText: String?, // Texto de atribuição
    val attributionHTML: String?, // HTML de atribuição
    val data: CharacterData?, // Dados dos personagens
    val etag: String? // ETag para cache
)

// Dados dos personagens
data class CharacterData(
    val offset: Int?, // Offset da resposta (para paginação)
    val limit: Int?, // Limite de itens retornados
    val total: Int?, // Total de itens disponíveis
    val count: Int?, // Número de itens retornados na resposta
    val results: List<CharacterModel>? // Lista de modelos de personagens
)

// Modelo de um personagem
data class CharacterModel(
    val id: Int?, // ID do personagem
    val name: String?, // Nome do personagem
    val description: String?, // Descrição do personagem
    val modified: String?, // Data da última modificação
    val resourceURI: String?, // URI para mais informações do personagem
    val urls: List<CharacterUrl>?, // URLs relacionadas ao personagem
    val thumbnail: CharacterImage?, // Imagem de thumbnail do personagem
    val comics: ComicInfo?, // Informações sobre HQs do personagem
    val stories: StoryInfo?, // Informações sobre histórias do personagem
    val events: EventInfo?, // Informações sobre eventos relacionados ao personagem
    val series: SeriesInfo? // Informações sobre séries relacionadas ao personagem
)

// URL relacionada ao personagem
data class CharacterUrl(
    val type: String?, // Tipo de URL (ex.: "wiki", "official")
    val url: String? // URL do recurso
)

// Imagem do personagem
data class CharacterImage(
    val path: String?, // Caminho da imagem
    val extension: String? // Extensão da imagem (ex.: "jpg", "png")
)

// Informações sobre HQs relacionadas ao personagem
data class ComicInfo(
    val available: Int?, // Número total de HQs disponíveis
    val returned: Int?, // Número de HQs retornadas na resposta
    val collectionURI: String?, // URI da coleção de HQs
    val items: List<ComicItem>? // Lista de itens de HQs
)

// Item de HQ
data class ComicItem(
    val resourceURI: String?, // URI do recurso da HQ
    val name: String? // Nome da HQ
)

// Informações sobre histórias relacionadas ao personagem
data class StoryInfo(
    val available: Int?, // Número total de histórias disponíveis
    val returned: Int?, // Número de histórias retornadas na resposta
    val collectionURI: String?, // URI da coleção de histórias
    val items: List<CharacterStoryItem>? // Lista de itens de histórias
)

// Item de história
data class CharacterStoryItem(
    val resourceURI: String?, // URI do recurso da história
    val name: String?, // Nome da história
    val type: String? // Tipo de história (ex.: "adventure", "mystery")
)

// Informações sobre eventos relacionados ao personagem
data class EventInfo(
    val available: Int?, // Número total de eventos disponíveis
    val returned: Int?, // Número de eventos retornados na resposta
    val collectionURI: String?, // URI da coleção de eventos
    val items: List<CharacterEventItem>? // Lista de itens de eventos
)

// Item de evento
data class CharacterEventItem(
    val resourceURI: String?, // URI do recurso do evento
    val name: String? // Nome do evento
)

// Informações sobre séries relacionadas ao personagem
data class SeriesInfo(
    val available: Int?, // Número total de séries disponíveis
    val returned: Int?, // Número de séries retornadas na resposta
    val collectionURI: String?, // URI da coleção de séries
    val items: List<SeriesItem>? // Lista de itens de séries
)

// Item de série
data class SeriesItem(
    val resourceURI: String?, // URI do recurso da série
    val name: String? // Nome da série
)
package br.com.marvel.data.model

// Modelo de resposta para a API de HQs
data class ComicResponseModel(
    val code: Int?, // Código de status HTTP da resposta
    val status: String?, // Status da resposta (ex.: "Ok", "Error")
    val copyright: String?, // Informações sobre direitos autorais
    val attributionText: String?, // Texto de atribuição
    val attributionHTML: String?, // HTML de atribuição
    val data: ComicData?, // Dados das HQs
    val etag: String? // ETag para cache
)

// Dados das HQs
data class ComicData(
    val offset: Int?, // Offset da resposta (para paginação)
    val limit: Int?, // Limite de itens retornados
    val total: Int?, // Total de itens disponíveis
    val count: Int?, // Número de itens retornados na resposta
    val results: List<ComicModel>? // Lista de modelos de HQs
)

// Modelo de uma HQ
data class ComicModel(
    val id: Int?, // ID da HQ
    val digitalId: Int?, // ID digital da HQ
    val title: String?, // Título da HQ
    val issueNumber: Double?, // Número da edição
    val variantDescription: String?, // Descrição da variante, se houver
    val description: String?, // Descrição da HQ
    val modified: String?, // Data da última modificação
    val isbn: String?, // ISBN da HQ
    val upc: String?, // UPC da HQ
    val diamondCode: String?, // Código Diamond da HQ
    val ean: String?, // EAN da HQ
    val issn: String?, // ISSN da HQ
    val format: String?, // Formato da HQ (ex.: "Comic", "TPB")
    val pageCount: Int?, // Número de páginas
    val textObjects: List<TextObject>?, // Objetos de texto relacionados
    val resourceURI: String?, // URI para mais informações da HQ
    val urls: List<Url>?, // URLs relacionadas à HQ
    val series: Series?, // Série à qual a HQ pertence
    val variants: List<Variant>?, // Variantes da HQ
    val collections: List<Collection>?, // Coleções que incluem a HQ
    val collectedIssues: List<CollectedIssue>?, // Edições coletadas da HQ
    val dates: List<Date>?, // Datas relevantes para a HQ
    val prices: List<Price>?, // Preços da HQ
    val thumbnail: Image?, // Imagem de thumbnail da HQ
    val images: List<Image>?, // Imagens adicionais da HQ
    val creators: Creators?, // Criadores envolvidos na HQ
    val characters: Characters?, // Personagens que aparecem na HQ
    val stories: Stories?, // Histórias relacionadas à HQ
    val events: Events?, // Eventos relacionados à HQ
    var favorite: Boolean? // Indicador se a HQ é favorita
)

// Objeto de texto associado à HQ
data class TextObject(
    val type: String?, // Tipo de objeto de texto (ex.: "issue", "cover")
    val language: String?, // Linguagem do texto
    val text: String? // Texto propriamente dito
)

// URL relacionada à HQ
data class Url(
    val type: String?, // Tipo de URL (ex.: "wiki", "official")
    val url: String? // URL do recurso
)

// Série à qual a HQ pertence
data class Series(
    val resourceURI: String?, // URI do recurso da série
    val name: String? // Nome da série
)

// Variante da HQ
data class Variant(
    val resourceURI: String?, // URI do recurso da variante
    val name: String? // Nome da variante
)

// Coleção que inclui a HQ
data class Collection(
    val resourceURI: String?, // URI do recurso da coleção
    val name: String? // Nome da coleção
)

// Edição coletada da HQ
data class CollectedIssue(
    val resourceURI: String?, // URI do recurso da edição coletada
    val name: String? // Nome da edição coletada
)

// Data relevante para a HQ
data class Date(
    val type: String?, // Tipo de data (ex.: "onsaleDate", "focDate")
    val date: String? // Data no formato ISO 8601
)

// Preço da HQ
data class Price(
    val type: String?, // Tipo de preço (ex.: "printPrice", "digitalPrice")
    val price: Float? // Valor do preço
)

// Imagem associada à HQ
data class Image(
    val path: String?, // Caminho da imagem
    val extension: String? // Extensão da imagem (ex.: "jpg", "png")
)

// Criadores envolvidos na HQ
data class Creators(
    val available: Int?, // Número total de criadores disponíveis
    val returned: Int?, // Número de criadores retornados na resposta
    val collectionURI: String?, // URI da coleção de criadores
    val items: List<CreatorItem>? // Lista de itens de criadores
)

// Item de criador
data class CreatorItem(
    val resourceURI: String?, // URI do recurso do criador
    val name: String?, // Nome do criador
    val role: String? // Papel do criador (ex.: "writer", "artist")
)

// Personagens que aparecem na HQ
data class Characters(
    val available: Int?, // Número total de personagens disponíveis
    val returned: Int?, // Número de personagens retornados na resposta
    val collectionURI: String?, // URI da coleção de personagens
    val items: List<CharacterItem>? // Lista de itens de personagens
)

// Item de personagem
data class CharacterItem(
    val resourceURI: String?, // URI do recurso do personagem
    val name: String?, // Nome do personagem
    val role: String? // Papel do personagem (ex.: "hero", "villain")
)

// Histórias relacionadas à HQ
data class Stories(
    val available: Int?, // Número total de histórias disponíveis
    val returned: Int?, // Número de histórias retornadas na resposta
    val collectionURI: String?, // URI da coleção de histórias
    val items: List<StoryItem>? // Lista de itens de histórias
)

// Item de história
data class StoryItem(
    val resourceURI: String?, // URI do recurso da história
    val name: String?, // Nome da história
    val type: String? // Tipo de história (ex.: "adventure", "origin")
)

// Eventos relacionados à HQ
data class Events(
    val available: Int?, // Número total de eventos disponíveis
    val returned: Int?, // Número de eventos retornados na resposta
    val collectionURI: String?, // URI da coleção de eventos
    val items: List<EventItem>? // Lista de itens de eventos
)

// Item de evento
data class EventItem(
    val resourceURI: String?, // URI do recurso do evento
    val name: String? // Nome do evento
)
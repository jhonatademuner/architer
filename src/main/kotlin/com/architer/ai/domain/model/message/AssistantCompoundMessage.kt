package com.architer.ai.domain.model.message

import com.fasterxml.jackson.annotation.*

data class AssistantCompoundMessage(
    override val role: String,
    var content: List<ContentItem>
) : AssistantBaseMessage {

    constructor(role: String, text: String, imageUrl: String?) : this(
        role = role,
        content = if (imageUrl != null) {
            listOf(
                TextContent(text = text),
                ImageUrlContent(imageUrl = ImageUrl(url = imageUrl))
            )
        } else {
            listOf(TextContent(text = text))
        }
    )

    override fun getTextMessage(): String {
        return content.filterIsInstance<TextContent>().joinToString(separator = " ") { it.text }
    }

    enum class ContentType {
        text, image_url
    }

    sealed class ContentItem {
        abstract val type: ContentType
    }

    data class TextContent(
        override val type: ContentType = ContentType.text,
        val text: String
    ) : ContentItem()

    data class ImageUrlContent(
        override val type: ContentType = ContentType.image_url,
        @JsonProperty("image_url")
        val imageUrl: ImageUrl
    ) : ContentItem()

    data class ImageUrl(
        val url: String
    )
}


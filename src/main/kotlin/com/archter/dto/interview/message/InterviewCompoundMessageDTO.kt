package com.archter.dto.interview.message

import com.archter.domain.interview.InterviewRole
import com.fasterxml.jackson.annotation.*

data class InterviewCompoundMessageDTO(
    override var role: InterviewRole,
    var content: List<ContentItem>
) : BaseMessageDTO {

    constructor(role: InterviewRole, text: String, imageUrl: String?) : this(
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

    fun getTextMessage(): String {
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

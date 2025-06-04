package com.archter.domain.challenge

import lombok.Data
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Data
@Document(collection = "challenge")
data class Challenge(

    @Id
    var id: UUID = UUID.randomUUID(),
    var name: String,
    var description: String,
    var functionalRequirements: List<String> = mutableListOf(),
    var nonFunctionalRequirements: List<String> = mutableListOf(),
    var constraints: List<String> = mutableListOf(),
    var discussionAreas: List<String> = mutableListOf(),
    var stretchTopics: List<String> = mutableListOf(),

    @CreatedDate
    var createdAt: Date = Date(),

    @LastModifiedDate
    var updatedAt: Date = Date(),

){
    override fun toString(): String {
        return buildString {
            appendLine("Interview Challenge")
            appendLine()
            appendLine("Name: $name")
            appendLine("Description: $description")
            appendLine()

            if (functionalRequirements.isNotEmpty()) {
                appendLine("Functional Requirements:")
                functionalRequirements.forEach { appendLine("  - $it") }
                appendLine()
            }

            if (nonFunctionalRequirements.isNotEmpty()) {
                appendLine("Non-Functional Requirements:")
                nonFunctionalRequirements.forEach { appendLine("  - $it") }
                appendLine()
            }

            if (constraints.isNotEmpty()) {
                appendLine("Constraints:")
                constraints.forEach { appendLine("  - $it") }
                appendLine()
            }

            if (discussionAreas.isNotEmpty()) {
                appendLine("Areas to Discuss:")
                discussionAreas.forEach { appendLine("  - $it") }
                appendLine()
            }

            if (stretchTopics.isNotEmpty()) {
                appendLine("Stretch Topics:")
                stretchTopics.forEach { appendLine("  - $it") }
                appendLine()
            }
        }
    }
}
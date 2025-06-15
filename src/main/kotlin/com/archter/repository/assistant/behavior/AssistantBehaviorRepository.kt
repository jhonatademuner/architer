package com.archter.repository.assistant.behavior

import com.archter.domain.assistant.behavior.AssistantBehavior
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AssistantBehaviorRepository : JpaRepository<AssistantBehavior, UUID> {
}
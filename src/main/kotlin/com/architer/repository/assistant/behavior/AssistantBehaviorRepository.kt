package com.architer.repository.assistant.behavior

import com.architer.domain.assistant.behavior.AssistantBehavior
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AssistantBehaviorRepository : JpaRepository<AssistantBehavior, UUID> {
}
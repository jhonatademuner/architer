package com.architer.repository.behavior

import com.architer.domain.behavior.Behavior
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BehaviorRepository : JpaRepository<Behavior, UUID> {
}
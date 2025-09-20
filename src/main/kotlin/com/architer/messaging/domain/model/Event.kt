package com.architer.messaging.domain.model

import java.util.UUID

interface Event {
    val eventId: UUID
    val timestamp: Long
}
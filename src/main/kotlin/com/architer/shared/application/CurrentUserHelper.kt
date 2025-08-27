package com.architer.shared.application

import java.util.UUID

interface CurrentUserHelper {
    fun getCurrentUserId(): UUID
}
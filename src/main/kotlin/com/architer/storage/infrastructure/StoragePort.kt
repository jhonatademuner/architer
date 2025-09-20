package com.architer.storage.infrastructure

import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface StoragePort {
    fun uploadInterviewDiagram(interviewId: UUID, image: MultipartFile): String
}
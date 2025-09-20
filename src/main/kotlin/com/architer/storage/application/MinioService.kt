package com.architer.storage.application

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.architer.storage.infrastructure.StoragePort
import com.architer.storage.infrastructure.config.MinioProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class MinioService(
    private val minioProperties: MinioProperties,
    private val amazonS3: AmazonS3
): StoragePort {
    override fun uploadInterviewDiagram(interviewId: UUID, image: MultipartFile): String {
        val key = "interviews/diagrams/$interviewId.png"
        val metadata = ObjectMetadata().apply {
            contentLength = image.size
            contentType = image.contentType ?: "image/png"
        }
        amazonS3.putObject(PutObjectRequest(minioProperties.bucket, key, image.inputStream, metadata))
        return amazonS3.getUrl(minioProperties.bucket, key).toString()
    }
}
package com.archter.service.minio

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.archter.config.property.MinioProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class MinioService(
    private val minioProperties: MinioProperties,
    private val amazonS3: AmazonS3
) {

    fun uploadInterviewSystemDesign(interviewId: UUID, file: MultipartFile): String {
        val key = "interviews/system-designs/$interviewId.png"

        val metadata = ObjectMetadata().apply {
            contentLength = file.size
            contentType = file.contentType ?: "image/png"
        }

        amazonS3.putObject(PutObjectRequest(minioProperties.bucket, key, file.inputStream, metadata))

        return amazonS3.getUrl(minioProperties.bucket, key).toString()
    }
}
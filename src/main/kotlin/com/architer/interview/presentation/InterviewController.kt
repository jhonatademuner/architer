package com.architer.interview.presentation

import com.architer.interview.application.InterviewService
import com.architer.interview.presentation.dto.InterviewCreateRequest
import com.architer.interview.presentation.dto.InterviewMessageResponse
import com.architer.interview.presentation.dto.InterviewResponse
import com.architer.interview.presentation.dto.InterviewSimplifiedResponse
import com.architer.interview.presentation.dto.InterviewMessageSendRequest
import com.architer.shared.model.PaginatedList
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/api/v1/interviews")
class InterviewController(
    private val service: InterviewService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: InterviewCreateRequest): InterviewSimplifiedResponse {
        return service.create(request)
    }

    @GetMapping
    fun findAll(
        @RequestParam page: Int = 1,
        @RequestParam size: Int = 10
    ): PaginatedList<InterviewSimplifiedResponse> {
        return service.findAll(page, size)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): InterviewResponse {
        return service.findById(id)
    }

    @PostMapping("/{interviewId}/finish")
    fun finish(@PathVariable interviewId: UUID) {
        service.finish(interviewId)
    }

    @PostMapping("/{interviewId}/messages", consumes = ["multipart/form-data"])
    fun sendMessage(
        @PathVariable interviewId: UUID,
        @RequestPart("message") message: InterviewMessageSendRequest,
        @RequestPart("image", required = false) image: MultipartFile?
    ): InterviewMessageResponse {
        return service.sendMessage(interviewId, message, image)
    }

    @GetMapping("/{interviewId}/messages")
    fun findMessagesByInterviewId(@PathVariable interviewId: UUID): List<InterviewMessageResponse> {
        return service.findMessagesByInterviewId(interviewId)
    }
}
package com.architer.controller.interview

import com.architer.dto.interview.InterviewDTO
import com.architer.dto.interview.InterviewUpdateDTO
import com.architer.dto.interview.message.InterviewMessageCreateDTO
import com.architer.dto.interview.message.InterviewMessageDTO
import com.architer.service.interview.InterviewService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/api")
class InterviewController(
    private val interviewService: InterviewService
) {

    @PostMapping("/v1/interview")
    fun create(@RequestParam challenge: UUID, @RequestParam behavior: UUID): ResponseEntity<InterviewDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(interviewService.create(challenge, behavior))
    }

    @PostMapping("/v1/interview/{interviewId}/message", consumes = ["multipart/form-data"])
    fun sendMessageWithImage(
        @PathVariable interviewId: UUID,
        @RequestPart("message") message: InterviewMessageCreateDTO,
        @RequestPart("image", required = false) image: MultipartFile?
    ): ResponseEntity<InterviewMessageDTO> {
        val response = interviewService.sendMessage(interviewId, message, image)
        return ResponseEntity.ok(response)
    }


    @GetMapping("/v1/interview")
    fun findAll(@RequestParam page: Int = 0, @RequestParam size: Int = 10): ResponseEntity<List<InterviewDTO>> {
        return ResponseEntity.status(HttpStatus.OK).body(interviewService.findAll(page, size))
    }

    @GetMapping("/v1/interview/{interviewId}")
    fun findById(@PathVariable interviewId: UUID): ResponseEntity<InterviewDTO> {
        return ResponseEntity.status(HttpStatus.OK).body(interviewService.findById(interviewId))
    }

    @PutMapping("/v1/interview")
    fun update(@RequestBody interviewUpdateDTO: InterviewUpdateDTO): ResponseEntity<InterviewDTO> {
        return ResponseEntity.status(HttpStatus.OK).body(interviewService.update(interviewUpdateDTO))
    }

    @DeleteMapping("/v1/interview/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        interviewService.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

}
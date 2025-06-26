package com.architer.controller.assistant.behavior

import com.architer.dto.assistant.behavior.AssistantBehaviorCreateDTO
import com.architer.dto.assistant.behavior.AssistantBehaviorDTO
import com.architer.dto.assistant.behavior.AssistantBehaviorUpdateDTO
import com.architer.service.assistant.behavior.AssistantBehaviorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
class AssistantBehaviorController(
    private val assistantBehaviorService: AssistantBehaviorService
) {

    @PostMapping("/v1/assistant/behavior")
    fun create(@RequestBody assistantBehavior: AssistantBehaviorCreateDTO): ResponseEntity<AssistantBehaviorDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(assistantBehaviorService.create(assistantBehavior))
    }

    @GetMapping("/v1/assistant/behavior")
    fun findAll(@RequestParam page: Int = 0, @RequestParam size: Int = 10): ResponseEntity<List<AssistantBehaviorDTO>> {
        return ResponseEntity.status(HttpStatus.OK).body(assistantBehaviorService.findAll(page, size))
    }

    @GetMapping("/v1/assistant/behavior/{id}")
    fun findById(@PathVariable id: UUID): ResponseEntity<AssistantBehaviorDTO> {
        return ResponseEntity.status(HttpStatus.OK).body(assistantBehaviorService.findById(id))
    }

    @PutMapping("/v1/assistant/behavior")
    fun update(@RequestBody assistantBehavior: AssistantBehaviorUpdateDTO): ResponseEntity<AssistantBehaviorDTO> {
        return ResponseEntity.status(HttpStatus.OK).body(assistantBehaviorService.update(assistantBehavior))
    }

    @DeleteMapping("/v1/assistant/behavior/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        assistantBehaviorService.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

}
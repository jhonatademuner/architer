package com.architer.controller.behavior

import com.architer.dto.behavior.BehaviorCreateDTO
import com.architer.dto.behavior.BehaviorDTO
import com.architer.dto.behavior.BehaviorUpdateDTO
import com.architer.dto.behavior.SimplifiedBehaviorDTO
import com.architer.service.behavior.BehaviorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
class BehaviorController(
    private val behaviorService: BehaviorService
) {

    @PostMapping("/v1/behaviors")
    fun create(@RequestBody assistantBehavior: BehaviorCreateDTO): ResponseEntity<BehaviorDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(behaviorService.create(assistantBehavior))
    }

    @GetMapping("/v1/behaviors")
    fun findAll(@RequestParam page: Int = 0, @RequestParam size: Int = 10): ResponseEntity<List<SimplifiedBehaviorDTO>> {
        return ResponseEntity.status(HttpStatus.OK).body(behaviorService.findAll(page, size))
    }

    @GetMapping("/v1/behaviors/{id}")
    fun findById(@PathVariable id: UUID): ResponseEntity<BehaviorDTO> {
        return ResponseEntity.status(HttpStatus.OK).body(behaviorService.findById(id))
    }

    @PutMapping("/v1/behaviors")
    fun update(@RequestBody assistantBehavior: BehaviorUpdateDTO): ResponseEntity<BehaviorDTO> {
        return ResponseEntity.status(HttpStatus.OK).body(behaviorService.update(assistantBehavior))
    }

    @DeleteMapping("/v1/behaviors/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        behaviorService.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

}
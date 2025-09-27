package com.architer.shared.presentation

import com.architer.shared.application.AppSettingService
import com.architer.shared.presentation.dto.AppSettingCreateRequest
import com.architer.shared.presentation.dto.AppSettingResponse
import com.architer.shared.presentation.dto.AppSettingUpdateRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/app-settings")
class AppSettingController(
    private val service: AppSettingService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: AppSettingCreateRequest): AppSettingResponse {
        return service.create(request)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): AppSettingResponse {
        return service.findById(id)
    }

    @GetMapping("/key/{settingKey}")
    fun findBySettingKey(@PathVariable settingKey: String): AppSettingResponse {
        return service.findBySettingKey(settingKey)
    }

    @PutMapping
    fun update(@RequestBody request: AppSettingUpdateRequest): AppSettingResponse {
        return service.update(request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        service.delete(id)
    }
}
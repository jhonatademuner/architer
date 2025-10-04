package com.architer.shared.application

import com.architer.shared.domain.repository.AppSettingRepository
import com.architer.shared.exception.ResourceNotFoundException
import com.architer.shared.infrastructure.AppSettingsPort
import com.architer.shared.presentation.dto.AppSettingCreateRequest
import com.architer.shared.presentation.dto.AppSettingResponse
import com.architer.shared.presentation.dto.AppSettingUpdateRequest
import com.architer.shared.presentation.mapper.AppSettingMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AppSettingService(
    private val repository: AppSettingRepository,
    private val mapper: AppSettingMapper
): AppSettingsPort {
    override fun create(request: AppSettingCreateRequest): AppSettingResponse {
        val entity = mapper.toEntity(request)
        return mapper.toResponse(repository.save(entity))
    }

    @Cacheable(value = ["appSettings"], key = "#id")
    override fun findById(id: UUID): AppSettingResponse {
        val entity = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("AppSetting not found") }
        return mapper.toResponse(entity)
    }

    @Cacheable(value = ["appSettings"], key = "#settingKey")
    override fun findBySettingKey(settingKey: String): AppSettingResponse {
        val entity = repository.findBySettingKey(settingKey)
            .orElseThrow { ResourceNotFoundException("AppSetting not found") }
        return mapper.toResponse(entity)
    }

    @CacheEvict(value = ["appSettings"], allEntries = true)
    override fun update(request: AppSettingUpdateRequest): AppSettingResponse {
        val existing = repository.findById(request.id)
            .orElseThrow { ResourceNotFoundException("AppSetting not found") }
        val updated = mapper.updateEntity(request, existing)
        return mapper.toResponse(repository.save(updated))
    }

    @CacheEvict(value = ["appSettings"], allEntries = true)
    override fun delete(id: UUID) {
        repository.deleteById(id)
    }
}
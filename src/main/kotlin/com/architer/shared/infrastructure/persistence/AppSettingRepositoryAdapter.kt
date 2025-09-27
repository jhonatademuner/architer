package com.architer.shared.infrastructure.persistence

import com.architer.shared.domain.model.AppSetting
import com.architer.shared.domain.repository.AppSettingRepository
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class AppSettingRepositoryAdapter(
    private val jpaRepository: JpaAppSettingRepository
): AppSettingRepository {
    override fun save(entity: AppSetting): AppSetting = jpaRepository.save(entity)
    override fun findById(id: UUID): Optional<AppSetting> = jpaRepository.findById(id)
    override fun findBySettingKey(settingKey: String): Optional<AppSetting> = jpaRepository.findBySettingKey(settingKey)
    override fun deleteById(id: UUID) = jpaRepository.deleteById(id)
}
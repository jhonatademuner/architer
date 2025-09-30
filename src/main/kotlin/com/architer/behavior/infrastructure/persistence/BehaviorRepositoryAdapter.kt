package com.architer.behavior.infrastructure.persistence

import com.architer.behavior.domain.model.Behavior
import com.architer.behavior.domain.repository.BehaviorRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class BehaviorRepositoryAdapter(
    private val jpaRepository: JpaBehaviorRepository
) : BehaviorRepository {
    override fun save(behavior: Behavior): Behavior = jpaRepository.save(behavior)
    override fun findAll(pageable: Pageable): Page<Behavior> = jpaRepository.findAllBy(pageable)
    override fun findById(id: UUID): Optional<Behavior> = jpaRepository.findById(id)
    override fun deleteById(id: UUID): Unit = jpaRepository.deleteById(id)
    override fun findAllByPublishedIsTrue(pageable: Pageable): Page<Behavior> = jpaRepository.findAllByPublishedIsTrue(pageable)
    override fun findByIdAndPublishedIsTrue(id: UUID): Optional<Behavior> = jpaRepository.findByIdAndPublishedIsTrue(id)
}
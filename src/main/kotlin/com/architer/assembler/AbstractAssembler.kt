package com.architer.assembler

import java.util.Collections
import java.util.stream.Collectors


/**
 * Generic abstract assembler for mapping between Entity and DTO.
 *
 * @param <E> Entity type
 * @param <D> DTO type
</D></E> */
abstract class AbstractAssembler<E, D> {
    /**
     * Convert an entity to a DTO.
     */
    abstract fun toDto(entity: E): D

    /**
     * Convert a DTO to an entity.
     */
    abstract fun toEntity(dto: D): E

    /**
     * Convert a list of entities to a list of DTOs.
     */
    fun toDtoList(entities: MutableList<E>): MutableList<D> {
        if (entities.isEmpty()) return Collections.emptyList()
        return entities.stream()
            .map<D?> { entity: E -> this.toDto(entity) }
            .collect(Collectors.toList())
    }

    /**
     * Convert a list of DTOs to a list of entities.
     */
    fun toEntityList(dtos: MutableList<D>): MutableList<E> {
        if (dtos.isEmpty()) return Collections.emptyList()
        return dtos.stream()
            .map<E?> { dto: D -> this.toEntity(dto) }
            .collect(Collectors.toList())
    }
}

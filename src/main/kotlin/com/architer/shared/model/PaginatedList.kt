package com.architer.shared.model

import org.springframework.data.domain.Page

data class PaginatedList<T>(
    val items: List<T>,
    val page: Int,
    val size: Int,
    val totalItems: Long,
    val totalPages: Int
) {
    companion object {
        fun <T> from(page: Page<T>): PaginatedList<T> {
            return PaginatedList(
                items = page.content,
                page = page.number+1,
                size = page.size,
                totalItems = page.totalElements,
                totalPages = page.totalPages
            )
        }
    }
}

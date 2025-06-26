package com.architer.repository.interview

import com.architer.domain.interview.Interview
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface InterviewRepository  : JpaRepository<Interview, UUID> {
}
package com.archter.repository.interview

import com.archter.domain.interview.Interview
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface InterviewRepository  : JpaRepository<Interview, UUID> {
}
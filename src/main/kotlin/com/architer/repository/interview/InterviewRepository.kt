package com.architer.repository.interview

import com.architer.domain.interview.Interview
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface InterviewRepository  : JpaRepository<Interview, UUID> {
}
package com.architer.repository.challenge

import com.architer.domain.challenge.Challenge
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChallengeRepository : JpaRepository<Challenge, UUID> {
}
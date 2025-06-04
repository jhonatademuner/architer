package com.archter.repository.challenge

import com.archter.domain.challenge.Challenge
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChallengeRepository : MongoRepository<Challenge, UUID> {
}
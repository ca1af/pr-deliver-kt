package com.example.prdeliver.message.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MessageRepository : JpaRepository<Message?, Long?> {
    fun findByMessengerId(messengerId: Long?): Message?

    fun deleteAllByMessengerId(messengerId: Long?)

    fun findAllByMessengerId(messengerId: Long?): List<Message?>
}

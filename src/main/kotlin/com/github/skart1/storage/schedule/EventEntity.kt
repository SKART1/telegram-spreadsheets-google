package com.github.skart1.storage.schedule

import java.time.LocalDateTime

interface EventEntity {
    fun getStartTime(): LocalDateTime
    fun getEndTime(): LocalDateTime

    fun getShortDescription(): String
    fun getLongDescription(): String

    abstract class AbstractEventEntity(private val startTime: LocalDateTime, private val endTime: LocalDateTime,
                                   private val shortDescription: String, private val longDescription: String) : EventEntity {
        override fun getStartTime(): LocalDateTime {
            return startTime
        }

        override fun getEndTime(): LocalDateTime {
            return endTime
        }

        override fun getShortDescription(): String {
            return shortDescription
        }

        override fun getLongDescription(): String {
            return longDescription
        }

    }
}
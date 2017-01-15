package com.github.skart1.storage.schedule

import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function

class ScheduleStorage {
    private var events: MutableSet<EventEntity> = ConcurrentHashMap.newKeySet()

    interface Filter : Function<EventEntity, Boolean>
    class DatePeriod(val leftBoundary: LocalDateTime, val rightBoundary: LocalDateTime) : Filter {
        override fun apply(t: EventEntity): Boolean {
            val startTime = t.getStartTime()

            return (startTime.isAfter(leftBoundary) || startTime.isEqual(leftBoundary))
                    && (startTime.isBefore(rightBoundary) || startTime.isEqual(leftBoundary))

        }
    }

    fun filter(filter: Filter): List<EventEntity> {
        return events.filter { filter.apply(it) }
    }

    fun setEvents(events: List<EventEntity>)  {
        this.events.removeAll(this.events)
        this.events.addAll(events)
    }

    fun getEvents(): Set<EventEntity> {
        return HashSet(events)
    }
}
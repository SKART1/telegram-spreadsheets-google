package com.github.skart1.storage.schedule

import java.time.LocalDateTime

class BreakEventEntity(startTime: LocalDateTime, endTime: LocalDateTime):
        EventEntity.AbstractEventEntity(startTime, endTime, "Перерыв", "Перерыв в работе")
package com.github.skart1.storage.schedule

import java.time.LocalDateTime

class SeanceEventEntity(startTime: LocalDateTime, endTime: LocalDateTime) :
        EventEntity.AbstractEventEntity(startTime, endTime, "Сеанс", "Сеанс игры")

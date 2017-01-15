package com.github.skart1.storage.schedule

import java.time.LocalDateTime

class VacantEventEntity(startTime: LocalDateTime, endTime: LocalDateTime):
        EventEntity.AbstractEventEntity(startTime, endTime, "Свободно", "Свободный слот")
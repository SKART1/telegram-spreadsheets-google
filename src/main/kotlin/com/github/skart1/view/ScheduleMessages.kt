package com.github.skart1.view

import com.github.skart1.storage.schedule.EventEntity
import com.github.skart1.storage.schedule.ScheduleStorage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun showScheduleWeeks(bot: TelegramBot, chatId: Long, limit: Int, scheduleStorage: ScheduleStorage) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM")
    val events = scheduleStorage.getEvents()

    fun hasEvents(leftBorder: LocalDateTime, rightBorder: LocalDateTime): Boolean {
        return events.find { it.getStartTime() > leftBorder && it.getEndTime() < rightBorder } != null
    }

    fun formMessage(): StringBuilder {
        val result = StringBuilder().append(WEEKS_LIST).append("\n")

        fun printWeek(i: Int, left: LocalDateTime, right: LocalDateTime) {
            result.append("$i. ${left.format(formatter)} - ${right.format(formatter)}")
        }

        var leftBorder = LocalDateTime.now()
        var rightBorder = LocalDate.now().atTime(LocalTime.MAX).with(DayOfWeek.SUNDAY)

        for(i in 0..limit - 1) {
            //if(hasEvents(leftBorder, rightBorder)) {
            if (i < WEEKS.size) {
                result.append("${i + 1}. ${WEEKS[i]} \n")
            } else {
                printWeek(i + 1, leftBorder, rightBorder)
            }
            leftBorder = leftBorder.toLocalDate().plusWeeks(1).atTime(LocalTime.MIN).with(DayOfWeek.MONDAY)
            rightBorder = leftBorder.toLocalDate().atTime(LocalTime.MAX).with(DayOfWeek.SUNDAY)
            //}
        }

        return result
    }


    val message: String
    if(events.isEmpty()) {
        message = "No"
    } else {
        message = formMessage().toString()
    }

    val response = SendMessage(chatId, message)
            .parseMode(ParseMode.HTML)
            .disableWebPagePreview(true)
            .disableNotification(true)

    bot.execute(response)
}

fun showInfo(bot: TelegramBot, chatId: Long, events: List<EventEntity>) {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    fun formMessage(): StringBuilder {
        val result = StringBuilder()

        //One item print
        fun printItem(item: EventEntity) {
            val start = item.getStartTime()
            val end = item.getEndTime()

            result.append(start.format(formatter)).append(" - ").append(end.format(formatter))
                    .append(" ").append(item.getShortDescription()).append("\n")
        }

        //Day print
        fun printDay(day: DayOfWeek) {
            result.append("\n").append("\n")
                    .append("<b>").append(day.toString()).append("</b>").append("\n")
        }

        //First
        val eventEntity = events[0]
        var currentDayOfWeek = eventEntity.getStartTime().dayOfWeek
        result.append("<b>").append(currentDayOfWeek).append("</b>").append("\n")

        //Others
        events.forEach {
            val newDayOfWeek = it.getStartTime().dayOfWeek
            if (newDayOfWeek == currentDayOfWeek) {
                printItem(it)
            } else {
                currentDayOfWeek = newDayOfWeek
                printDay(newDayOfWeek)
                printItem(it)
            }
        }
        return result
    }


    val message: String
    if(events.isEmpty()) {
        message = "No events"
    } else {
        message = formMessage().toString()
    }

    val response = SendMessage(chatId, message)
            .parseMode(ParseMode.HTML)
            .disableWebPagePreview(true)
            .disableNotification(true)

    bot.execute(response)
}


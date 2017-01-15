package com.github.skart1.state

import com.github.skart1.storage.schedule.ScheduleStorage
import com.github.skart1.view.*
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


class ScheduleSelectedState(val scheduleStorage: ScheduleStorage) : State {
    override fun newUpdate(ableToState: AbleToState, stateInstanceProvider: StateInstanceProvider, bot: TelegramBot, update: Update) {
        val message = update.message()
        val text = message.text()
        val chatId = message.chat().id()

        when (text) {
            Commands.CANCEL.text -> {
                showReadme(bot, chatId)
                ableToState.putState(stateInstanceProvider.getInitialStateInstance())
            }
            Commands.HELP.text -> {
                showHelp(bot, chatId)
            }
            else -> {
                try {
                    val week = text.toInt()

                    if(week > 3) {
                        throw IndexOutOfBoundsException()
                    }
                    when (week) {
                        1 -> {
                            val start = LocalDateTime.now()
                            val end = LocalDate.now().atTime(LocalTime.MAX).with(DayOfWeek.SUNDAY)
                            showInfo(bot, chatId, scheduleStorage.filter(ScheduleStorage.DatePeriod(start, end)))
                            ableToState.putState(stateInstanceProvider.getInitialStateInstance())
                        }
                        else -> {
                            val start = LocalDate.now().atTime(LocalTime.MIN).with(DayOfWeek.MONDAY).plusWeeks(week.toLong() - 1)
                            val end = start.toLocalDate().atTime(LocalTime.MAX).with(DayOfWeek.SUNDAY)
                            showInfo(bot, chatId, scheduleStorage.filter(ScheduleStorage.DatePeriod(start, end)))
                            ableToState.putState(stateInstanceProvider.getInitialStateInstance())
                        }
                    }
                } catch (e: IndexOutOfBoundsException) {
                    wrongIndex(text, bot, chatId)
                    showScheduleWeeks(bot, chatId, 3, scheduleStorage)
                } catch (e: NumberFormatException) {
                    stateRetry(text, bot, chatId)
                    showScheduleWeeks(bot, chatId, 3, scheduleStorage)
                }
            }
        }
    }

    fun wrongIndex(text: String, bot: TelegramBot, chatId: Long) {
        val response = SendMessage(chatId, "Некорректное значение \"$text\". Введите номер недели или $ENTER_CANCEL_COMMAND")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)

        bot.execute(response)
    }


    fun stateRetry(text: String, bot: TelegramBot, chatId: Long) {
        val response = SendMessage(chatId, "Неопознанный текст \"$text\". Введите номер недели или ${ENTER_CANCEL_COMMAND}")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)

        bot.execute(response)
    }
}
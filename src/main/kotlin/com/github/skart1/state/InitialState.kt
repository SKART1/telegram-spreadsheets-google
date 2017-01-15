package com.github.skart1.state

import com.github.skart1.storage.game.GameStorage
import com.github.skart1.storage.schedule.ScheduleStorage
import com.github.skart1.view.*
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update

class InitialState(private val admins: List<Int>,
                   private val gameStorage: GameStorage,
                   private val scheduleStorage: ScheduleStorage) : State {

    override fun newUpdate(ableToState: AbleToState, stateInstanceProvider: StateInstanceProvider, bot: TelegramBot, update: Update) {
        val message = update.message()
        val userId = message.from().id()
        val text = message.text()
        val chatId = message.chat().id()

        when (text) {
            Commands.HELP.text -> {
                showHelp(bot, chatId)
            }
            Commands.GAMES.text -> {
                showGamesList(bot, chatId, gameStorage)
                ableToState.putState(stateInstanceProvider.getGamesListSelectedState())
            }
            Commands.SCHEDULE.text -> {
                showScheduleWeeks(bot, chatId, 3, scheduleStorage)
                ableToState.putState(stateInstanceProvider.getScheduleSelectedState())
            }
            Commands.UPDATE.text -> {
                if(admins.contains(userId)) {
                    sendUpdate(bot, chatId)
                } else {
                    unrecognizedReset(bot, chatId)
                }
            }
            else -> {
                unrecognizedReset(bot, chatId)
            }
        }
    }
}

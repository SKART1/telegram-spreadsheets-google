package com.github.skart1.state

import com.github.skart1.storage.UserStorage
import com.github.skart1.storage.game.GameStorage
import com.github.skart1.view.sendUpdate
import com.github.skart1.view.showGamesList
import com.github.skart1.view.showHelp
import com.github.skart1.view.unrecognizedReset
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update

class SelectingGenresState(var gameStorage: GameStorage, val userStorage: UserStorage): State {

    override fun newUpdate(ableToState: AbleToState, stateInstanceProvider: StateInstanceProvider, bot: TelegramBot, update: Update) {
        val message = update.message()
        val userId = message.from().id()
        val text = message.text()
        val chatId = message.chat().id()

        val user = userStorage.getUser(userId)

        when (text) {
            Commands.CANCEL.text -> {
                user.resetGenresRequest()
                ableToState.putState(stateInstanceProvider.getInitialStateInstance())
            }
            else -> {
                try {
                    val gameSequentialNumber = (text.toInt() - 1)
                    val genres = gameStorage.getCurrentGenres()
                    if(gameSequentialNumber < -1 || gameSequentialNumber > genres.size) {

                    }


                    val game = gameStorage.getGame(gameSequentialNumber)
                    if(game == null) {
                        showGameNotFound(bot, chatId)
                    } else {
                        showGameInfo(game, bot, chatId)
                        ableToState.putState(stateInstanceProvider.getInitialStateInstance())
                    }
                } catch (e: IndexOutOfBoundsException) {
                    wrongIndex(text, bot, chatId)
                    showGamesList(bot, chatId, gameStorage)
                } catch (e: NumberFormatException) {
                    sayRetry(text, bot, chatId)
                    showGamesList(bot, chatId, gameStorage)
                }
            }
        }
    }
}
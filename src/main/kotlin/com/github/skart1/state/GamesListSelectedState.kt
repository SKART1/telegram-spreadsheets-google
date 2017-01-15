package com.github.skart1.state

import com.github.skart1.storage.game.GameEntity
import com.github.skart1.storage.game.GameStorage
import com.github.skart1.view.ENTER_CANCEL_COMMAND
import com.github.skart1.view.sendMessage
import com.github.skart1.view.showGamesList
import com.github.skart1.view.showReadme
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.request.SendPhoto

class GamesListSelectedState(var gameStorage: GameStorage): State {


    override fun newUpdate(ableToState: AbleToState, stateInstanceProvider: StateInstanceProvider, bot: TelegramBot, update: Update) {
        val chatId = update.message().chat().id()
        val message = update.message()
        val text = message.text()

        when (text) {
            Commands.CANCEL.text -> {
                showReadme(bot, chatId)
                ableToState.putState(stateInstanceProvider.getInitialStateInstance())
            }
            else -> {
                try {
                    val gameSequentialNumber = (text.toInt() - 1)
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

    private fun showGameNotFound(bot: TelegramBot, chatId: Long) {
        sendMessage(bot, chatId, "Игра не найдена! Выберите игру или $ENTER_CANCEL_COMMAND\n")
        showGamesList(bot, chatId, gameStorage)
    }

    private fun showGameInfo(gameEntity: GameEntity, bot: TelegramBot, chatId: Long) {
        val mainResponse = SendMessage(chatId, gameEntity.shortName + " " + gameEntity.longName)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
        bot.execute(mainResponse)

        gameEntity.imageEntities.listIterator().forEach {
            val imageResponse = SendPhoto(chatId, it.link)
                    .caption(it.comment)
            bot.execute(imageResponse)
        }
    }

    fun sayRetry(text: String, bot: TelegramBot, chatId: Long) {
        val response = SendMessage(chatId, "Неопознанный текст \"$text\". Введите номер игры или ${ENTER_CANCEL_COMMAND}")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)

        bot.execute(response)
    }

    fun wrongIndex(text: String, bot: TelegramBot, chatId: Long) {
        val response = SendMessage(chatId, "Некорректное значение \"$text\". Введите номер игры или ${ENTER_CANCEL_COMMAND}")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)

        bot.execute(response)
    }
}
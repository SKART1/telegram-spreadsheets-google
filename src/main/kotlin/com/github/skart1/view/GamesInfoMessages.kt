package com.github.skart1.view

import com.github.skart1.storage.game.GameEntity
import com.github.skart1.storage.game.GameStorage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import java.util.*

fun unrecognizedReset(bot: TelegramBot, chatId: Long) {
    sendMessage(bot, chatId, ENTER_COMMAND_STARTING_WITH)
}


fun showGamesListWithTextPrefix(bot: TelegramBot, chatId: Long, gameStorage: GameStorage, prefix: String) {
    val games = gameStorage.getGames()
    val responseStringBuilder = StringBuilder()

    responseStringBuilder.append(prefix)
    responseStringBuilder.append(AVAILABLE_GAMES_LIST).append("\n")
    val keyboardButtons = ArrayList<KeyboardButton>()
    keyboardButtons.add(KeyboardButton(BACK))
    games.forEachIndexed { i, gameEntity ->
        responseStringBuilder.append(i + 1).append(". ").append(gameEntity.name).append("\n")
        keyboardButtons.add(KeyboardButton((i + 1).toString()))
    }

    val keyboard = ReplyKeyboardMarkup(keyboardButtons.toArray(arrayOfNulls<KeyboardButton>(keyboardButtons.size)))
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)

    val response = SendMessage(chatId, responseStringBuilder.toString())
            .parseMode(ParseMode.HTML)
            .disableWebPagePreview(true)
            .disableNotification(true)
    //.replyMarkup(keyboard)

    bot.execute(response)
}


fun showGamesList(bot: TelegramBot, chatId: Long, gameStorage: GameStorage) {
    showGamesListWithTextPrefix(bot, chatId, gameStorage, "")
}

fun sendMessage(bot: TelegramBot, chatId: Long, message: String) {
    val response = SendMessage(chatId, message)
            .parseMode(ParseMode.HTML)
            .disableWebPagePreview(true)
            .disableNotification(true)

    bot.execute(response)
}

fun showHelp(bot: TelegramBot, chatId: Long) {
    val response = SendMessage(chatId, HELP_MESSAGE)
            .parseMode(ParseMode.HTML)
            .disableWebPagePreview(true)
            .disableNotification(true)

    bot.execute(response)
}

fun showReadme(bot: TelegramBot, chatId: Long) {
    val response = SendMessage(chatId, "Введите /help для списка команд")
            .parseMode(ParseMode.HTML)
            .disableWebPagePreview(true)
            .disableNotification(true)

    bot.execute(response)
}

fun displayGenres(bot: TelegramBot, chatId: Long, genres: Set<GameEntity.Genre>) {
    val resultStr = StringBuilder()

    fun appendRow(row: Int, genre: GameEntity.Genre) {
        resultStr.append(row).append(". ").append(genre.string).append("\n")
    }
    genres.forEachIndexed(::appendRow)

    val response = SendMessage(chatId, resultStr.toString())
            .parseMode(ParseMode.HTML)
            .disableWebPagePreview(true)
            .disableNotification(true)
    bot.execute(response)
}

fun sendUpdate(bot: TelegramBot, chatId: Long) {
    val response = SendMessage(chatId, "Данные обновлены")
            .parseMode(ParseMode.HTML)
            .disableWebPagePreview(true)
            .disableNotification(true)

    bot.execute(response)
}


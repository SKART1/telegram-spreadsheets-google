package com.github.skart1

import com.github.skart1.state.GamesListSelectedState
import com.github.skart1.state.InitialState
import com.github.skart1.state.SelectingGenresState
import com.github.skart1.state.StateInstanceProvider
import com.github.skart1.storage.UserStorage
import com.github.skart1.storage.game.GameStorage
import com.pengrad.telegrambot.TelegramBotAdapter
import java.util.*

fun main(args: Array<String>) {
    val FILE_NAME = "config.properties"
    val TELEGRAM_BOT_TOKEN = "telegramBotToken"
    val ADMINS = "admins"

    val SPREADSHEET_ID = "spreadsheetId"

    fun read(configFile: String): Properties {
        val conf = Properties()
        Thread.currentThread().contextClassLoader.getResourceAsStream(configFile).use {
            resourceStream -> conf.load(resourceStream)
        }
        return conf
    }

    fun validate(conf: Properties): Properties {
        fun validateProperty(name: String) {
            if(!conf.containsKey(name)) {
                throw IllegalStateException("Property $name is not set in $FILE_NAME")
            }
        }
        validateProperty(SPREADSHEET_ID)
        validateProperty(TELEGRAM_BOT_TOKEN)
        validateProperty(ADMINS)
        return conf
    }

    val conf = validate(read(FILE_NAME))
    val spreadsheetId = conf.getProperty(SPREADSHEET_ID)
    val admins = conf.getProperty(ADMINS, "").split(",").filter { it != "" }.map(String::trim).map(String::toInt)
    val token = conf.getProperty(TELEGRAM_BOT_TOKEN)


    val main = Main(spreadsheetId, admins, token)
    main.run()


    //Data watcher


    //Bot watcher

}

private class Main(private val spreadsheetId: String, admins: List<Int>, token: String) : StateInstanceProvider, Runnable {
    private val users = UserStorage(this)
    private val gameStorage = GameStorage()

    private val initialState =  InitialState(admins, gameStorage)
    private val gameListSelectedState =  GamesListSelectedState(gameStorage)
    private val bot = TelegramBotAdapter.build(token)

    override fun run() {
        val polService = PollService(spreadsheetId, gameStorage)
        polService.start()

        bot.setGetUpdatetsListener { updates ->
            updates.forEach {
                val message = it.message()
                val userId = message.from().id()
                val user = users.getUser(userId)
                user.state.newUpdate(user, this, bot, it)
            }

            System.out.println(updates.toString())
            com.pengrad.telegrambot.GetUpdatesListener.CONFIRMED_UPDATES_ALL
        }
    }

    override fun getInitialStateInstance(): InitialState {
        return initialState
    }

    override fun getGamesListSelectedState(): GamesListSelectedState {
        return gameListSelectedState
    }

    override fun getSelectingGenresState(): SelectingGenresState {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}





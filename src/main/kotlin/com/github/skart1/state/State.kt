package com.github.skart1.state

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update

interface State {
    fun newUpdate(ableToState: AbleToState, stateInstanceProvider: StateInstanceProvider,
                  bot: TelegramBot, update: Update)
}
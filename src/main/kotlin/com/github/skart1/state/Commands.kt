package com.github.skart1.state

import com.github.skart1.view.*

enum class Commands(var text: String, var description: String) {
    HELP(HELP_COMMAND, HELP_COMMAND_DESCRIPTION),
    GAMES(GAMES_COMMAND, GAMES_COMMAND_DESCRIPTION),
    SCHEDULE(SCHEDULE_COMMAND, SCHEDULE_COMMAND_DESCRIPTION),
    UPDATE(UPDATE_COMMAND, UPDATE_COMMAND_DESCRIPTION),
    CANCEL(CANCEL_COMMAND, CANCEL_COMMAND_DESCRIPTION);

    override fun toString(): String {
        return text
    }
}
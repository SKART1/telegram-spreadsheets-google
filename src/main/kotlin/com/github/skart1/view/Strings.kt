package com.github.skart1.view

import com.github.skart1.state.Commands

val AVAILABLE_GAMES_LIST = "<b>Список доступных игр: </b>"
val BACK = "←"

val ENTER_COMMAND_STARTING_WITH = "Введите команде начинающуюся с \"/\""
val ENTER_CANCEL_COMMAND: String = "наберите ${Commands.CANCEL} чтобы вернуться назад"


//Help message
val HELP_MESSAGE: String = "Я могу помочь вам предоставить информацию о клубе виртуальной реальность <b>VRGame</b>.\n\n" +
        "Список доступных команд:\n" +
        "${Commands.HELP} - ${Commands.HELP.description}\n" +
        "${Commands.GAMES} - ${Commands.GAMES.description}\n" +
        "${Commands.SCHEDULE} - ${Commands.SCHEDULE.description}\n" +
        "${Commands.CANCEL} - ${Commands.CANCEL.description}\n"


//Weeks
val WEEKS_LIST = "<b>Распиание в пределах недели: </b>"
val WEEKS = arrayOf("Текущая неделя", "Следующая неделя", "Послеследующая неделя")

package com.github.skart1

import com.github.skart1.storage.game.GameEntity


class GameParser(internal val rowIndex: Int, internal val row: List<String>) {
    val parseMessages = StringBuilder()
    var error: ErrorLevel = ErrorLevel.NONE

    private var name: String = ""
    private var description: String = ""
    private var genres: Set<GameEntity.Genre>? = null
    private var coop: GameEntity.Coop? = null
    private var difficulty: GameEntity.Difficulty? = null
    private var age: GameEntity.Age? = null
    private var room: Set<GameEntity.Room>? = null

    fun parse() {
        fun getName(): String {
            val index = 0
            return if (row[index].isNullOrBlank()) {
                parseMessages.append("Row $rowIndex. Field \"name\" in column \"$index\" is null or blank").append("\n")
                error += ErrorLevel.ERROR
                ""
            } else {
                row[index]
            }
        }

        fun getDescription(): String {
            val index = 1
            return if (row[index].isNullOrEmpty()) {
                parseMessages.append("Row $rowIndex. Field \"description\". Column \"$index\" is column \"$index\" is null or empty").append("\n")
                error += ErrorLevel.WARNING
                ""
            } else {
                row[index]
            }
        }

        fun getGenres(): Set<GameEntity.Genre> {
            val leftIndex = 2
            val rightIndex = 5
            fun getGenre(columnIndex: Int, columnValue: String): GameEntity.Genre? {
                try {
                    return GameEntity.Genre.getEnumValue(columnValue)
                } catch (e: IllegalArgumentException) {
                    parseMessages.append("Row $rowIndex. Field \"genre\" was not parsed in column \"$columnIndex\": ").append(e.message).append("\n")
                    error += ErrorLevel.WARNING
                    return null
                }
            }

            val result = row.subList(leftIndex, rightIndex).mapIndexed({i, genre -> getGenre(leftIndex + i, genre)}).filterNotNull().toSet()
            if (result.isEmpty()) {
                parseMessages.append("Row $rowIndex. No genres found for in columns between \"$leftIndex\" and \"${rightIndex - 1}\"").append("\n")
                error += ErrorLevel.ERROR
            }
            return result
        }

        fun getCoop(): GameEntity.Coop? {
            val index = 5
            try {
                return GameEntity.Coop.getEnumValue(row[index])
            } catch (e: IllegalArgumentException) {
                parseMessages.append("Row $rowIndex. Field \"coop\" in column \"$index\" is not parsed correctly: ${e.message}").append("\n")
                error += ErrorLevel.ERROR
                return null
            }
        }

        fun getDifficulty(): GameEntity.Difficulty? {
            val index = 6
            try {
                return GameEntity.Difficulty.getEnumValue(row[index])
            } catch (e: IllegalArgumentException) {
                parseMessages.append("Row $rowIndex. Field \"difficulty\" in column \"$index\" is not parsed correctly: ${e.message}").append("\n")
                error += ErrorLevel.ERROR
                return null
            }
        }

        fun getAge(): GameEntity.Age? {
            val index = 7
            try {
                return GameEntity.Age.getEnumValue(row[index])
            } catch (e: IllegalArgumentException) {
                parseMessages.append("Row $rowIndex. Field \"age\" in column \"$index\" is not parsed correctly: ${e.message}").append("\n")
                error += ErrorLevel.ERROR
                return null
            }
        }

        fun getRoom(): Set<GameEntity.Room>? {
            val leftIndex = 8
            val rightIndex = 10
            fun getRoom(columnIndex: Int, columnValue: String): GameEntity.Room? {
                try {
                    return GameEntity.Room.getEnumValue(columnValue)
                } catch (e: IllegalArgumentException) {
                    parseMessages.append("Row $rowIndex. Field \"room\" was not parsed in column \"$columnIndex\": ").append(e.message).append("\n")
                    error += ErrorLevel.WARNING
                    return null
                }
            }

            val result = row.subList(leftIndex, rightIndex).mapIndexed({i, room -> getRoom(leftIndex + i, room)}).filterNotNull().toSet()
            if (result.isEmpty()) {
                parseMessages.append("Row $rowIndex. No rooms found for in columns between \"$leftIndex\" and \"${rightIndex - 1}\"").append("\n")
                error += ErrorLevel.ERROR
            }
            return result

        }

        if (row.isEmpty()) {
            error += ErrorLevel.ERROR
            parseMessages.append("Row is empty")
        } else {
            name = getName()
            description = getDescription()
            genres = getGenres()
            coop = getCoop()
            difficulty = getDifficulty()
            age = getAge()
            room = getRoom()

        }
    }


    fun getResult(): GameEntity {
        if(error == ErrorLevel.ERROR) throw IllegalStateException("Parsing has error. Object not created")
        return GameEntity(name, description, genres!!, coop!!, difficulty!!, age!!, room!!)
    }


    enum class ErrorLevel {
        NONE,
        WARNING,
        ERROR;

        operator fun plus(right: ErrorLevel): ErrorLevel {
            return when (this)  {
                NONE -> {
                    if (right == WARNING) {
                        WARNING
                    } else if (right == ERROR) {
                        ERROR
                    } else {
                        this
                    }
                }
                WARNING -> {
                    if (right == ERROR) {
                        ERROR
                    } else {
                        this
                    }
                }
                ERROR -> {
                    this
                }
            }
        }
    }
}
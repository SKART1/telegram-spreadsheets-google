package com.github.skart1.storage.game

import java.util.*

data class GameEntity(val name: String, val description: String, val genres: Set<Genre>,
                      val coop: Coop, val difficulty: Difficulty, val age: Age, val room: Set<Room>) {
    enum class Genre(val string: String) {
        Shooting("стрелялки"),
        Extreme("экстрим");

        companion object {
            @JvmStatic private val map: MutableMap<String, Genre> = HashMap()

            init {
                for(genre in Genre.values()) {
                    map.put(genre.string, genre)
                }
            }

            @JvmStatic fun getEnumValue(string: String): Genre {
                return map[string] ?: throw IllegalArgumentException("Unknown String Value: " + string)
            }
        }
    }

    enum class Coop(val string: String) {
        Shooting("да"),
        Extreme("нет");

        companion object {
            @JvmStatic private val map: MutableMap<String, Coop> = HashMap()

            init {
                for(coop in Coop.values()) {
                    map.put(coop.string, coop)
                }
            }

            @JvmStatic fun getEnumValue(string: String): Coop {
                return map[string] ?: throw IllegalArgumentException("Unknown String Value: " + string)
            }
        }
    }

    enum class Difficulty(val string: String) {
        Shooting("да"),
        Extreme("нет");

        companion object {
            @JvmStatic private val map: MutableMap<String, Difficulty> = HashMap()

            init {
                for(difficulty in Difficulty.values()) {
                    map.put(difficulty.string, difficulty)
                }
            }

            @JvmStatic fun getEnumValue(string: String): Difficulty {
                return map[string] ?: throw IllegalArgumentException("Unknown String Value: " + string)
            }
        }
    }

    enum class Age(val string: String) {

    }

    enum class Room(val string: String) {

    }
}



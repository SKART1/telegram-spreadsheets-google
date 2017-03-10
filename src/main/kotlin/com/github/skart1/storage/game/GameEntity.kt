package com.github.skart1.storage.game

import java.util.*

data class GameEntity(val name: String, val description: String, val genres: Set<Genre>,
                      val coop: Coop, val difficulty: Difficulty, val age: Age, val room: Set<Room>) {
    enum class Genre(val string: String) {
        Shooting("стрелялки"),
        Extreme("экстрим"),
        Sport("спорт");

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
        Yes("да"),
        No("нет");

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
        Easy("простая"),
        Middle("средняя"),
        Hard("сложнаая");

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
        NoLimit("без ограничений");

        companion object {
            @JvmStatic private val map: MutableMap<String, Age> = HashMap()

            init {
                for(age in Age.values()) {
                    map.put(age.string, age)
                }
            }

            @JvmStatic fun getEnumValue(string: String): Age {
                return map[string] ?: throw IllegalArgumentException("Unknown String Value: " + string)
            }
        }
    }

    enum class Room(val string: String) {
        RedRoom("красная комната"),
        BlueRoom("синяя комната");

        companion object {
            @JvmStatic private val map: MutableMap<String, Room> = HashMap()

            init {
                for(room in Room.values()) {
                    map.put(room.string, room)
                }
            }

            @JvmStatic fun getEnumValue(string: String): Room {
                return map[string] ?: throw IllegalArgumentException("Unknown String Value: " + string)
            }
        }
    }
}



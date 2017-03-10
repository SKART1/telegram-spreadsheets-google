package com.github.skart1

import com.github.skart1.storage.game.GameEntity
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class GameParserTest {
    @Test
    fun testCreation() {
        val colons: List<String>  = ArrayList()
        GameParser(1, colons)
    }

    @Test
    fun testEmptyListErrorLevelParsing() {
        val colons: List<String>  = ArrayList()

        val gameParser = GameParser(1, colons)
        gameParser.parse()
        assertEquals(GameParser.ErrorLevel.ERROR, gameParser.error)
    }

    @Test(expected = IllegalStateException::class)
    fun testEmptyListExceptionInGettingAfterParsing() {
        val colons: List<String>  = ArrayList()

        val gameParser = GameParser(1, colons)
        gameParser.parse()
        gameParser.getResult()
    }

    @Test
    fun testSimpleString() {
        val colons: MutableList<String> = generateGameToParse()

        val gameParser = GameParser(1, colons)
        gameParser.parse()
        assertEquals(GameParser.ErrorLevel.NONE, gameParser.error)
        val game = gameParser.getResult()
        assertNotNull(game)

        assertEquals("Название", game.name)
        assertEquals("Описание", game.description)
        assertEquals(setOf(GameEntity.Genre.Extreme, GameEntity.Genre.Shooting, GameEntity.Genre.Sport), game.genres)
        assertEquals(GameEntity.Coop.Yes, game.coop)
        assertEquals(GameEntity.Difficulty.Middle, game.difficulty)
        assertEquals(GameEntity.Age.NoLimit, game.age)
        assertEquals(setOf(GameEntity.Room.BlueRoom, GameEntity.Room.RedRoom), game.room)
    }

    @Test(expected = IllegalStateException::class)
    fun testErrorInCaptionFatalError() {
        val colons: MutableList<String> = generateGameToParse()
        colons[0] = ""

        val gameParser = GameParser(1, colons)
        gameParser.parse()
        assertEquals(GameParser.ErrorLevel.ERROR, gameParser.error)
        gameParser.getResult()
    }


    @Test
    fun testWarningInDescription() {
        val colons: MutableList<String> = generateGameToParse()
        colons[1] = ""

        val gameParser = GameParser(1, colons)
        gameParser.parse()
        assertEquals(GameParser.ErrorLevel.WARNING, gameParser.error)
        gameParser.getResult()
    }

    @Test
    fun testMisspelledGenres() {
        val colons: MutableList<String> = generateGameToParse()
        colons[2] = "notAGenre"

        val gameParser = GameParser(1, colons)
        gameParser.parse()
        assertEquals(GameParser.ErrorLevel.WARNING, gameParser.error)
        gameParser.getResult()
    }


    @Test(expected = IllegalStateException::class)
    fun testNoGenres() {
        val colons: MutableList<String> = generateGameToParse()
        colons[2] = ""
        colons[3] = ""
        colons[4] = ""

        val gameParser = GameParser(1, colons)
        gameParser.parse()
        assertEquals(GameParser.ErrorLevel.ERROR, gameParser.error)
        gameParser.getResult()
    }


    private fun generateGameToParse(): MutableList<String> {
        val colons: MutableList<String> = ArrayList()
        colons.add("Название")
        colons.add("Описание")
        colons.add("экстрим")
        colons.add("стрелялки")
        colons.add("спорт")
        colons.add("да")
        colons.add("средняя")
        colons.add("без ограничений")
        colons.add("синяя комната")
        colons.add("красная комната")
        return colons
    }
}
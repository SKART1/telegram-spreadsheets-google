package com.github.skart1.storage.game

import java.util.*
import java.util.concurrent.ConcurrentHashMap

class GameStorage {
    private var games: MutableSet<GameEntity> = ConcurrentHashMap.newKeySet()
    private val currentGenres: MutableSet<GameEntity.Genre> = ConcurrentHashMap.newKeySet()


    fun getGame(index: Int): GameEntity? {
        return games.elementAt(index)
    }

    fun getGames(): Set<GameEntity> {
        return HashSet(games)
    }

    fun getCurrentGenres(): Set<GameEntity.Genre> {
        return HashSet(currentGenres)
    }

    fun setGames(games: List<GameEntity>) {
        this.games.removeAll(this.games)
        this.games.addAll(games)

        currentGenres.removeAll(currentGenres)
        currentGenres.addAll(games.map { it -> HashSet<GameEntity.Genre>(it.genres) }
                .reduce { left, right -> left.addAll(right); left })
    }


}
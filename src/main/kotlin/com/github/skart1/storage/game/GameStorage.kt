package com.github.skart1.storage.game

import java.util.*
import java.util.concurrent.ConcurrentHashMap

class GameStorage {
    private var games: MutableSet<GameEntity> = ConcurrentHashMap.newKeySet()

    fun getGame(index: Int): GameEntity? {
        return games.elementAt(index)
    }

    fun getGames(): Set<GameEntity> {
        return HashSet(games)
    }

    fun setGames(games: List<GameEntity>)  {
        this.games.removeAll(this.games)
        this.games.addAll(games)
    }
}
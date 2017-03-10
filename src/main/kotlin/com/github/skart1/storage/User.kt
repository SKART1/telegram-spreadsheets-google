package com.github.skart1.storage

import com.github.skart1.state.AbleToState
import com.github.skart1.state.State
import com.github.skart1.state.StateInstanceProvider
import com.github.skart1.storage.game.GameEntity
import java.util.*

data class User (val userId: Int, private val stateInstanceProvider : StateInstanceProvider): AbleToState {
    @Volatile var state: State = stateInstanceProvider.getInitialStateInstance()
    @Volatile var genresRequest: GenresRequest? = null

    override fun putState(state: State) {
        this.state = state
    }

    fun resetGenresRequest() {
        genresRequest = null
    }

    fun startRequest(genres: Set<GameEntity.Genre>) {
        genresRequest = GenresRequest(genres)
    }

    fun addToGenreToRequest(genre: GameEntity.Genre) {
        genresRequest!!.requestSet.add(genre)
    }

    class GenresRequest(var genres: Set<GameEntity.Genre>) {
        var requestSet: MutableSet<GameEntity.Genre> = HashSet()
    }
}
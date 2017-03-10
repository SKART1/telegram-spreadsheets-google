package com.github.skart1.state

interface StateInstanceProvider {
    fun getInitialStateInstance(): InitialState
    fun getGamesListSelectedState(): GamesListSelectedState
    fun getSelectingGenresState(): SelectingGenresState
}
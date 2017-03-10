package com.github.skart1.state

import com.github.skart1.storage.game.GameStorage
import org.junit.Test
import org.mockito.Mockito.mock

class GamesListSelectedStateTest {
    @Test
    fun showGenresTest() {
        val storage = mock(GameStorage::class.java)

        val state = GamesListSelectedState(storage)
        //state.newUpdate()
    }
}
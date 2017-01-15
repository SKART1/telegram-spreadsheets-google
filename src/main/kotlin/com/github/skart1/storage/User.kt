package com.github.skart1.storage

import com.github.skart1.state.AbleToState
import com.github.skart1.state.State
import com.github.skart1.state.StateInstanceProvider

data class User (val userId: Int, private val stateInstanceProvider : StateInstanceProvider): AbleToState {
    @Volatile var state: State = stateInstanceProvider.getInitialStateInstance()

    override fun putState(state: State) {
        this.state = state
    }
}
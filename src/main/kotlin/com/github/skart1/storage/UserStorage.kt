package com.github.skart1.storage

import com.github.skart1.state.StateInstanceProvider
import java.util.concurrent.ConcurrentHashMap

class UserStorage(private var stateInstanceProvider: StateInstanceProvider) {
    private var usersMap: MutableMap<Int, User>  = ConcurrentHashMap()

    fun getUser(userId: Int) : User {
        return usersMap.getOrPut(userId, { User(userId, stateInstanceProvider) })
    }
}



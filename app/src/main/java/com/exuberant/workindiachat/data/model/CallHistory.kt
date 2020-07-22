package com.exuberant.workindiachat.data.model

enum class State {
    INCOMING,
    OUTGOING,
    MISSED
}

data class CallHistory(
    val state: State,
    val time: String
)
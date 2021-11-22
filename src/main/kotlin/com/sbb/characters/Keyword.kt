package com.sbb.characters

import com.sbb.Board

sealed class Keyword {
    object Flying : Keyword()

    class Support(
        val attack: Long = 0,
        val health: Long = 0,
        val traits: List<Trait>? = null,
    ) : Keyword()

    class LastBreath(val onLastBreath: (Board, Int) -> Unit): Keyword()

    class Slay(val onSlay: (Board) -> Unit): Keyword()
}

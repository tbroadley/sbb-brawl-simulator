package com.sbb.characters

import com.sbb.Board

sealed class Keyword {
    object Flying : Keyword()
    class Support(val health: Long) : Keyword()
    class LastBreath(val onLastBreath: (Board, Int) -> Unit): Keyword()
}

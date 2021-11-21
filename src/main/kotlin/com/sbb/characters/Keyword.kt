package com.sbb.characters

import com.sbb.Board

sealed class Keyword {
    object FLYING : Keyword()
    class SUPPORT(val health: Long) : Keyword()
    class LAST_BREATH(val onLastBreath: (Board, Int) -> Unit): Keyword()
}

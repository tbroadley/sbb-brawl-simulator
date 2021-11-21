package com.sbb.characters

sealed class Keyword {
    object FLYING : Keyword()
    class SUPPORT(val health: Long) : Keyword()
}

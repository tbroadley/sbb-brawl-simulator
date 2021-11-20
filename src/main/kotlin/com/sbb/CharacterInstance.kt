package com.sbb

data class CharacterInstance(
    val character: Character,
) {
    var attack = character.baseAttack
    var health = character.baseHealth
}

fun Character.toInstance() = CharacterInstance(this)

package com.sbb

class CharacterInstance(
    val character: Character,
) {
    var attack = character.baseAttack
    var health = character.baseHealth

    override fun toString(): String {
        return "CharacterInstance(character=$character, attack=$attack, health=$health)"
    }
}

fun Character.toInstance() = CharacterInstance(this)

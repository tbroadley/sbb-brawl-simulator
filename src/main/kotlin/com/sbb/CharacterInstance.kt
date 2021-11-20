package com.sbb

import java.util.*

class CharacterInstance(
    val character: Character,
) {
    val id: UUID = UUID.randomUUID()

    var attack = character.baseAttack
    var health = character.baseHealth

    override fun toString(): String {
        return "CharacterInstance(character=$character, id=$id, attack=$attack, health=$health)"
    }
}

fun Character.toInstance() = CharacterInstance(this)

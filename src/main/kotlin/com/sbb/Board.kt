package com.sbb

import kotlin.random.Random

data class Board(
    val positions: MutableList<CharacterInstance?>,
) {
    constructor(vararg positions: CharacterInstance?) : this(
        (positions.toList() + arrayOfNulls<CharacterInstance>(7 - positions.size)).toMutableList()
    )

    var nextAttackerIndex: Int = 0

    fun isEmpty() = positions.all { it == null }

    fun nextAttacker() = positions[nextAttackerIndex]!!

    fun randomFrontRowCharacter() = positions.take(4).randomCharacter()
    fun randomCharacter(): CharacterInstance? = positions.randomCharacter()

    fun remove(character: CharacterInstance) {
        for (index in positions.indices) {
            if (positions[index] == character) {
                positions[index] = null
            }
        }
    }

    fun updateNextAttackerIndex(attacker: CharacterInstance) {
        check(!isEmpty()) { "Board has no next attacker" }

        for (index in 0 until 7) {
            if (positions[nextAttackerIndex] != null && positions[nextAttackerIndex] != attacker) break

            nextAttackerIndex += 1
            nextAttackerIndex %= positions.size
        }
    }
}

fun List<CharacterInstance?>.randomCharacter(): CharacterInstance? {
    val characters = filterNotNull()
    if (characters.isEmpty()) return null

    return characters[Random.nextInt(characters.size)]
}

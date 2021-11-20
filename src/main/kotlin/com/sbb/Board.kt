package com.sbb

import kotlin.random.Random

data class Board(
    val positions: MutableList<CharacterInstance?>,
) {
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

    fun updateNextAttackerIndex() {
        check(!isEmpty()) { "Board has no next attacker" }

        while(positions[nextAttackerIndex] == null) {
            nextAttackerIndex = (nextAttackerIndex + 1) % positions.size
        }
    }
}

fun List<CharacterInstance?>.randomCharacter(): CharacterInstance? {
    if (isEmpty()) return null

    return this[Random.nextInt(size)]
}

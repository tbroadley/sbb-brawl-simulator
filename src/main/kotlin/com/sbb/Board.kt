package com.sbb

import com.sbb.characters.CharacterInstance
import com.sbb.probability.Distribution
import com.sbb.treasures.Treasure

data class Board(
    val hero: Hero,
    val treasures: List<Treasure> = listOf(),
    val positions: MutableList<CharacterInstance?>,
) {
    constructor(hero: Hero) : this(
        hero = hero,
        positions = arrayOfNulls<CharacterInstance>(7).toMutableList()
    )

    override fun toString() = hero.humanReadableName

    fun isEmpty() = positions.all { it == null }
    fun hasNoAttackers() = positions.all { it == null || it.attack == 0L }

    fun getPositionOf(character: CharacterInstance) = positions.indexOf(character)

    fun remove(character: CharacterInstance) {
        val position = getPositionOf(character)
        positions[position] = null

        for (indexInFront in position.positionsInFront()) {
            for (support in character.character.supports()) {
                positions[indexInFront]?.removeSupport(support)
            }
        }

        character.character.onLastBreath(this, position)
    }

    var nextAttackerIndex: Int = 0
    fun nextAttacker() = positions[nextAttackerIndex]
    fun updateNextAttackerIndex(attacker: CharacterInstance) {
        check(!hasNoAttackers()) { "Board has no next attacker" }

        for (index in 0 until 7) {
            val nextAttacker = positions[nextAttackerIndex]
            if (nextAttacker != null && nextAttacker != attacker && nextAttacker.attack > 0) break

            nextAttackerIndex += 1
            nextAttackerIndex %= positions.size
        }
    }

    fun frontRowCharacterDistribution() = positions.take(4).uniformDistribution()
    fun backRowCharacterDistribution() = positions.drop(4).uniformDistribution()
    fun characterDistribution() = positions.uniformDistribution()
}

private fun List<CharacterInstance?>.uniformDistribution(): Distribution<CharacterInstance?> {
    val characters = filterNotNull()
    if (characters.isEmpty()) return Distribution.from(null to 1.0)

    return Distribution.from(characters.associateWith { 1.0 / characters.size })
}

fun Int.positionsInFront(): List<Int> {
    return when (this) {
        4 -> listOf(0, 1)
        5 -> listOf(1, 2)
        6 -> listOf(2, 3)
        0, 1, 2, 3 -> listOf()
        else -> throw IllegalArgumentException("$this is an invalid position index")
    }
}

fun Int.positionsBehind(): List<Int> {
    return when (this) {
        0 -> listOf(4)
        1 -> listOf(4, 5)
        2 -> listOf(5, 6)
        3 -> listOf(6)
        4, 5, 6 -> listOf()
        else -> throw IllegalArgumentException("$this is an invalid position index")
    }
}

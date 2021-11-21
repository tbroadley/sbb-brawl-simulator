package com.sbb

import com.sbb.characters.CharacterInstance
import com.sbb.probability.Distribution

data class Board(
    val hero: Hero,
    val positions: MutableList<CharacterInstance?>,
) {
    constructor(hero: Hero) : this(hero, arrayOfNulls<CharacterInstance>(7).toMutableList())

    override fun toString() = hero.humanReadableName

    fun getPositionOf(character: CharacterInstance) = positions.indexOf(character)

    var nextAttackerIndex: Int = 0

    fun isEmpty() = positions.all { it == null }

    fun nextAttacker() = positions[nextAttackerIndex]!!

    fun frontRowCharacterDistribution() = positions.take(4).uniformDistribution()
    fun backRowCharacterDistribution() = positions.drop(4).uniformDistribution()
    fun characterDistribution() = positions.uniformDistribution()

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

private fun List<CharacterInstance?>.uniformDistribution(): Distribution<CharacterInstance?> {
    val characters = filterNotNull()
    if (characters.isEmpty()) return Distribution.from(null to 1.0)

    return Distribution.from(characters.associateWith { 1.0 / characters.size })
}

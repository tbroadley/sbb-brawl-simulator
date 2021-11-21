package com.sbb.characters

import com.sbb.Board
import com.sbb.characters.Keyword.Support

class CharacterInstance(
    private val board: Board,
    val character: Character,
    var attack: Long = character.baseAttack,
    var health: Long = character.baseHealth,
) {
    override fun toString(): String {
        return "${board.hero.humanReadableName}'s ${character.humanReadableName} " +
                "in position ${board.getPositionOf(this) + 1} " +
                "with $attack attack and $health health"
    }

    fun addSupport(support: Support) {
        if (!hasMatchingTrait(support)) return

        attack += support.attack
        health += support.health
    }

    fun removeSupport(support: Support) {
        if (!hasMatchingTrait(support)) return

        // TODO I think this is incorrect. Research what actually happens in game.
        attack -= support.attack
        attack = attack.coerceAtLeast(0)

        // TODO I think this is incorrect. Research what actually happens in game.
        health -= support.health
        health = health.coerceAtLeast(1)
    }

    private fun hasMatchingTrait(support: Support): Boolean {
        return support.traits == null || support.traits.intersect(character.traits).isNotEmpty()
    }
}

fun Character.toInstance(board: Board) = CharacterInstance(board, this)

fun Character.toInstance(board: Board, attack: Long, health: Long): CharacterInstance {
    return CharacterInstance(
        board = board,
        character = this,
        attack = attack,
        health = health,
    )
}

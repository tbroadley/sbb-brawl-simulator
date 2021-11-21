package com.sbb.characters

import com.sbb.Board
import com.sbb.characters.Keyword.Support

class CharacterInstance(
    private val board: Board,
    val character: Character,
) {
    var attack = character.baseAttack
    var health = character.baseHealth

    override fun toString(): String {
        return "${board.hero.humanReadableName}'s ${character.humanReadableName} " +
                "in position ${board.getPositionOf(this) + 1} " +
                "with $attack attack and $health health"
    }

    fun applySupport(support: Support) {
        health += support.health
    }

    fun removeSupport(support: Support) {
        // TODO I think this is incorrect. Research what actually happens in game.
        health -= support.health
        health = health.coerceAtLeast(1)
    }
}

fun Character.toInstance(board: Board) = CharacterInstance(board, this)

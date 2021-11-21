package com.sbb

class CharacterInstance(
    private val board: Board,
    val character: Character,
) {
    var attack = character.baseAttack
    var health = character.baseHealth

    override fun toString(): String {
        return "${character.humanReadableName} " +
                "in position ${board.getPositionOf(this) + 1} " +
                "with $attack attack and $health health"
    }
}

fun Character.toInstance(board: Board) = CharacterInstance(board, this)

package com.sbb.characters

import com.sbb.Board
import com.sbb.characters.Keyword.*
import com.sbb.characters.Trait.DWARF
import com.sbb.positionsBehind

enum class Character(
    val humanReadableName: String,
    val baseAttack: Long,
    val baseHealth: Long,
    val keywords: List<Keyword> = listOf(),
    val traits: List<Trait> = listOf(),
) {
    B_A_A_D_BILLY_GRUFF(
        humanReadableName = "B-a-a-d Billy Gruff",
        baseAttack = 2,
        baseHealth = 3,
    ),
    BABY_DRAGON(
        humanReadableName = "Baby Dragon",
        baseAttack = 3,
        baseHealth = 2,
        keywords = listOf(Flying),
    ),
    BABY_ROOT(
        humanReadableName = "Baby Root",
        baseAttack = 0,
        baseHealth = 3,
        keywords = listOf(Support(health = 3)),
    ),
    BLACK_CAT(
        humanReadableName = "Black Cat",
        baseAttack = 1,
        baseHealth = 1,
        keywords = listOf(
            LastBreath { board, position ->
                board.positions[position] = CAT.toInstance(board)
                for (indexBehind in position.positionsBehind()) {
                    val supports = board.positions[indexBehind]?.character?.supports() ?: continue

                    for (support in supports) {
                        board.positions[position]?.applySupport(support)
                    }
                }
            },
        ),
    ),
    CAT(
        humanReadableName = "Cat",
        baseAttack = 1,
        baseHealth = 1,
    ),
    BLIND_MOUSE(
        humanReadableName = "Blind Mouse",
        baseAttack = 2,
        baseHealth = 2,
    ),
    CINDER_ELLA(
        humanReadableName = "Cinder-Ella",
        baseAttack = 2,
        baseHealth = 2,
    ),
    CRAFTY(
        humanReadableName = "Crafty",
        baseAttack = 1,
        baseHealth = 1,
        traits = listOf(DWARF),
    ),
    FANNY(
        humanReadableName = "Fanny",
        baseAttack = 2,
        baseHealth = 2,
        keywords = listOf(
            Support(
                attack = 2,
                health = 2,
                traits = listOf(DWARF)
            )
        ),
        traits = listOf(DWARF),
    ),
    TINY(
        humanReadableName = "Tiny",
        baseAttack = 6,
        baseHealth = 1,
        traits = listOf(DWARF),
    ),
    ;

    fun supports(): List<Support> {
        return keywords.filterIsInstance<Support>()
    }

    fun onLastBreath(board: Board, position: Int) {
        for (lastBreath in keywords.filterIsInstance<LastBreath>()) {
            lastBreath.onLastBreath(board, position)
        }
    }
}

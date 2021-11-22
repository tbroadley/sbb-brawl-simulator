package com.sbb.characters

import com.sbb.Board
import com.sbb.characters.Keyword.*
import com.sbb.characters.Trait.DWARF

enum class Character(
    val humanReadableName: String,
    val keywords: List<Keyword> = listOf(),
    val traits: List<Trait> = listOf(),
) {
    B_A_A_D_BILLY_GRUFF(
        humanReadableName = "B-a-a-d Billy Gruff",
    ),
    BABY_DRAGON(
        humanReadableName = "Baby Dragon",
        keywords = listOf(Flying),
    ),
    BABY_ROOT(
        humanReadableName = "Baby Root",
        keywords = listOf(Support(health = 3)),
    ),
    BLACK_CAT(
        humanReadableName = "Black Cat",
        keywords = listOf(
            LastBreath { board, position ->
                board.summon(CAT.toInstance(board, attack = 1, health = 1), position)
            },
        ),
    ),
    CAT(
        humanReadableName = "Cat",
    ),
    BLIND_MOUSE(
        humanReadableName = "Blind Mouse",
    ),
    CINDER_ELLA(
        humanReadableName = "Cinder-Ella",
    ),
    CRAFTY(
        humanReadableName = "Crafty",
        traits = listOf(DWARF),
    ),
    FANNY(
        humanReadableName = "Fanny",
        keywords = listOf(
            Support(
                attack = 2,
                health = 2,
                traits = listOf(DWARF)
            )
        ),
        traits = listOf(DWARF),
    ),
    GOLDEN_CHICKEN(
        humanReadableName = "Golden Chicken",
    ),
    HAPPY_LITTLE_TREE(
        humanReadableName = "Happy Little Tree",
    ),
    HUMPTY_DUMPTY(
        humanReadableName = "Humpty Dumpty",
    ),
    KITTY_CUTPURSE(
        humanReadableName = "Kitty Cutpurse",
        keywords = listOf(
            Slay { board ->
                board.goldEarned += 1
            }
        ),
    ),
    TINY(
        humanReadableName = "Tiny",
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

    fun onSlay(board: Board) {
        for (slay in keywords.filterIsInstance<Slay>()) {
            slay.onSlay(board)
        }
    }
}

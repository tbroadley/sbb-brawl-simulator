package com.sbb

import com.sbb.Brawl.Result.*
import kotlin.random.Random

fun simulate(brawl: Brawl): Brawl.Result {
    var attackingBoard = if (Random.nextBoolean()) brawl.board1 else brawl.board2
    println("$attackingBoard goes first")

    while (true) {
        val defendingBoard = if (attackingBoard == brawl.board1) brawl.board2 else brawl.board1

        val attacker = attackingBoard.nextAttacker()
        val defender = defendingBoard.randomFrontRowCharacter() ?: defendingBoard.randomCharacter()!!

        println(
            "${attacker.character.humanReadableName} " +
                    "in position ${attackingBoard.nextAttackerIndex} " +
                    "attacks ${defender.character.humanReadableName} " +
                    "in position ${defendingBoard.getPositionOf(defender)}"
        )

        defender.health -= attacker.attack
        attacker.health -= defender.attack

        if (attacker.health <= 0) {
            attackingBoard.remove(attacker)
        }

        if (defender.health <= 0) {
            defendingBoard.remove(defender)
        }

        if (brawl.board1.isEmpty() && brawl.board2.isEmpty()) return TIE
        if (brawl.board1.isEmpty()) return BOARD2_WIN
        if (brawl.board2.isEmpty()) return BOARD1_WIN

        attackingBoard.updateNextAttackerIndex(attacker)

        attackingBoard = defendingBoard
    }
}

class Simulator {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val brawl = Brawl(
                board1 = Board(
                    positions = mutableListOf(
                        Character.B_A_A_D_BILLY_GRUFF.toInstance(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                    ),
                ),
                board2 = Board(
                    positions = mutableListOf(
                        Character.B_A_A_D_BILLY_GRUFF.toInstance(),
                        Character.B_A_A_D_BILLY_GRUFF.toInstance(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                    ),
                ),
            )

            println(simulate(brawl))
        }
    }
}

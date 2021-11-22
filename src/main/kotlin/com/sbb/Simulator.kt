package com.sbb

import com.sbb.Brawl.Result.*
import com.sbb.characters.Character.*
import com.sbb.characters.Keyword.Flying
import com.sbb.characters.toInstance
import com.sbb.heroes.Hero.APOCALYPSE
import com.sbb.heroes.Hero.SIR_GALAHAD
import com.sbb.probability.Distribution

fun simulate(brawl: Brawl): Brawl.Result {
    var attackingBoard = Distribution.from(brawl.board1 to 0.5, brawl.board2 to 0.5).sample()
    println("$attackingBoard goes first")

    while (true) {
        val defendingBoard = if (attackingBoard == brawl.board1) brawl.board2 else brawl.board1

        val attacker = attackingBoard.nextAttacker()
        if (attacker == null) {
            attackingBoard = defendingBoard
            continue
        }

        val defenderDistribution = if (Flying in attacker.character.keywords) {
            defendingBoard.backRowCharacterDistribution()
        } else {
            defendingBoard.frontRowCharacterDistribution()
        }
        val defender = defenderDistribution.sample() ?: defendingBoard.characterDistribution().sample()!!

        println("$attacker attacks $defender")

        defender.health -= attacker.attack
        attacker.health -= defender.attack

        if (attacker.health <= 0) {
            attackingBoard.remove(attacker)

            println("$attacker dies")
        }

        if (defender.health <= 0) {
            defendingBoard.remove(defender)
            attacker.character.onSlay(attackingBoard)

            println("$defender dies")
        }

        if (brawl.board1.isEmpty() && brawl.board2.isEmpty()) return TIE
        if (brawl.board1.hasNoAttackers() && brawl.board2.hasNoAttackers()) return TIE
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
            val board1 = Board(APOCALYPSE)
            board1.setStartingPositions(
                0 to KITTY_CUTPURSE.toInstance(board1, attack = 1, health = 1),
            )

            val board2 = Board(SIR_GALAHAD)
            board2.setStartingPositions(
                0 to KITTY_CUTPURSE.toInstance(board2, attack = 1, health = 1),
            )

            val brawl = Brawl(board1 = board1, board2 = board2)

            when (simulate(brawl)) {
                BOARD1_WIN -> {
                    println("$board1 wins")
                }
                BOARD2_WIN -> {
                    println("$board2 wins")
                }
                TIE -> {
                    println("Tie")
                }
            }

            println("$board1 gold earned: ${board1.goldEarned}")
            println("$board2 gold earned: ${board2.goldEarned}")
        }
    }
}

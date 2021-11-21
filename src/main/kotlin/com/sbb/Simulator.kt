package com.sbb

import com.sbb.Brawl.Result.*
import com.sbb.Hero.APOCALYPSE
import com.sbb.Hero.SIR_GALAHAD
import com.sbb.characters.Character.*
import com.sbb.characters.Keyword.Flying
import com.sbb.characters.toInstance
import com.sbb.probability.Distribution

fun simulate(brawl: Brawl): Brawl.Result {
    brawl.board1.applySupport()
    brawl.board2.applySupport()

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

            println("$defender dies")
        }

        if (brawl.board1.hasNoAttackers() && brawl.board2.hasNoAttackers()) return TIE
        if (brawl.board1.hasNoAttackers()) return BOARD2_WIN
        if (brawl.board2.hasNoAttackers()) return BOARD1_WIN

        attackingBoard.updateNextAttackerIndex(attacker)

        attackingBoard = defendingBoard
    }
}

class Simulator {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val board1 = Board(APOCALYPSE)
            board1.positions[0] = BLACK_CAT.toInstance(board1)
            board1.positions[4] = BABY_ROOT.toInstance(board1)

            val board2 = Board(SIR_GALAHAD)
            board2.positions[0] = B_A_A_D_BILLY_GRUFF.toInstance(board2)
            board2.positions[4] = B_A_A_D_BILLY_GRUFF.toInstance(board2)

            val brawl = Brawl(board1 = board1, board2 = board2)
            val result = simulate(brawl)

            when(result) {
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
        }
    }
}

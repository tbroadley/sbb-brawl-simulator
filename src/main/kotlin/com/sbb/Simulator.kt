package com.sbb

import com.sbb.Brawl.Result.*
import com.sbb.Hero.APOCALYPSE
import com.sbb.Hero.SIR_GALAHAD
import com.sbb.characters.Character
import com.sbb.characters.Character.BABY_DRAGON
import com.sbb.characters.Character.B_A_A_D_BILLY_GRUFF
import com.sbb.characters.Keyword.FLYING
import com.sbb.characters.toInstance
import com.sbb.probability.Distribution

fun simulate(brawl: Brawl): Brawl.Result {
    var attackingBoard = Distribution.from(brawl.board1 to 0.5, brawl.board2 to 0.5).sample()
    println("$attackingBoard goes first")

    while (true) {
        val defendingBoard = if (attackingBoard == brawl.board1) brawl.board2 else brawl.board1

        val attacker = attackingBoard.nextAttacker()

        val defenderDistribution = if (FLYING in attacker.character.keywords) {
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
            val board1 = Board(APOCALYPSE)
            board1.positions[0] = BABY_DRAGON.toInstance(board1)

            val board2 = Board(SIR_GALAHAD)
            board2.positions[0] = B_A_A_D_BILLY_GRUFF.toInstance(board2)
            board2.positions[4] = B_A_A_D_BILLY_GRUFF.toInstance(board2)

            val brawl = Brawl(board1 = board1, board2 = board2)
            println(simulate(brawl))
        }
    }
}

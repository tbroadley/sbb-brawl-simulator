package com.sbb

import com.sbb.Hero.APOCALYPSE
import com.sbb.characters.Character.*
import com.sbb.characters.toInstance
import com.sbb.probability.Distribution
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BoardTest {
    @Test
    fun `next attacker is correctly calculated when the attacker survives`() {
        val board = Board(APOCALYPSE)

        val attacker = TINY.toInstance(board)
        val nextAttacker = TINY.toInstance(board)

        board.positions[0] = attacker
        board.positions[1] = nextAttacker

        board.updateNextAttackerIndex(attacker)
        assertEquals(nextAttacker, board.nextAttacker())
        assertEquals(1, board.nextAttackerIndex)
    }

    @Test
    fun `next attacker is correctly calculated when the attacker dies`() {
        val board = Board(APOCALYPSE)

        val attacker = TINY.toInstance(board)
        val nextAttacker = TINY.toInstance(board)

        board.positions[1] = nextAttacker

        board.updateNextAttackerIndex(attacker)
        assertEquals(nextAttacker, board.nextAttacker())
        assertEquals(1, board.nextAttackerIndex)
    }

    @Test
    fun `next attacker is correctly calculated when another unit replaces the dead attacker`() {
        val board = Board(APOCALYPSE)

        val attacker = TINY.toInstance(board)
        val replacement = B_A_A_D_BILLY_GRUFF.toInstance(board)
        val nextAttacker = TINY.toInstance(board)

        board.positions[0] = replacement
        board.positions[1] = nextAttacker

        board.updateNextAttackerIndex(attacker)
        assertEquals(replacement, board.nextAttacker())
        assertEquals(0, board.nextAttackerIndex)
    }

    @Test
    fun `next attacker is calculated correctly when there is a gap`() {
        val board = Board(APOCALYPSE)

        val firstAttacker = TINY.toInstance(board)
        val secondAttacker = B_A_A_D_BILLY_GRUFF.toInstance(board)
        val thirdAttacker = TINY.toInstance(board)

        board.positions[0] = firstAttacker
        board.positions[2] = secondAttacker
        board.positions[6] = thirdAttacker

        board.updateNextAttackerIndex(firstAttacker)
        assertEquals(secondAttacker, board.nextAttacker())
        assertEquals(2, board.nextAttackerIndex)

        board.updateNextAttackerIndex(secondAttacker)
        assertEquals(thirdAttacker, board.nextAttacker())
        assertEquals(6, board.nextAttackerIndex)

        board.updateNextAttackerIndex(thirdAttacker)
        assertEquals(firstAttacker, board.nextAttacker())
        assertEquals(0, board.nextAttackerIndex)
    }

    @Test
    fun `attack order skips units with no attack`() {
        val board = Board(APOCALYPSE)

        val firstAttacker = TINY.toInstance(board)
        val babyRoot = BABY_ROOT.toInstance(board)
        val secondAttacker = TINY.toInstance(board)

        board.positions[0] = firstAttacker
        board.positions[1] = babyRoot
        board.positions[2] = secondAttacker

        board.updateNextAttackerIndex(firstAttacker)
        assertEquals(secondAttacker, board.nextAttacker())
        assertEquals(2, board.nextAttackerIndex)
    }

    @Test
    fun `correct character is removed`() {
        val board = Board(APOCALYPSE)

        val character1 = TINY.toInstance(board)
        val character2 = TINY.toInstance(board)

        board.positions[0] = character1
        board.positions[2] = character2

        board.remove(character1)
        assertEquals(
            listOf(
                null,
                null,
                character2,
                null,
                null,
                null,
                null,
            ),
            board.positions
        )
    }

    @Test
    fun `distributions are correct for empty board`() {
        val board = Board(APOCALYPSE)

        assertEquals(Distribution.from(null to 1.0), board.characterDistribution())
        assertEquals(Distribution.from(null to 1.0), board.frontRowCharacterDistribution())
        assertEquals(Distribution.from(null to 1.0), board.backRowCharacterDistribution())
    }

    @Test
    fun `distributions are correct for board with only front-row characters`() {
        val board = Board(APOCALYPSE)

        val character1 = TINY.toInstance(board)
        val character2 = BABY_DRAGON.toInstance(board)

        board.positions[0] = character1
        board.positions[2] = character2

        assertEquals(
            Distribution.from(character1 to 0.5, character2 to 0.5),
            board.characterDistribution(),
        )
        assertEquals(
            Distribution.from(character1 to 0.5, character2 to 0.5),
            board.frontRowCharacterDistribution(),
        )
        assertEquals(Distribution.from(null to 1.0), board.backRowCharacterDistribution())
    }

    @Test
    fun `distributions are correct for board with only back-row characters`() {
        val board = Board(APOCALYPSE)

        val character1 = TINY.toInstance(board)
        val character2 = BABY_DRAGON.toInstance(board)
        val character3 = B_A_A_D_BILLY_GRUFF.toInstance(board)

        board.positions[4] = character1
        board.positions[5] = character2
        board.positions[6] = character3

        assertEquals(
            Distribution.from(character1 to 1.0 / 3, character2 to 1.0 / 3, character3 to 1.0 / 3),
            board.characterDistribution(),
        )
        assertEquals(Distribution.from(null to 1.0), board.frontRowCharacterDistribution())
        assertEquals(
            Distribution.from(character1 to 1.0 / 3, character2 to 1.0 / 3, character3 to 1.0 / 3),
            board.backRowCharacterDistribution(),
        )
    }

    @Test
    fun `distributions are correct for board with a mix of front- and back-row characters`() {
        val board = Board(APOCALYPSE)

        val character1 = TINY.toInstance(board)
        val character2 = BABY_DRAGON.toInstance(board)
        val character3 = B_A_A_D_BILLY_GRUFF.toInstance(board)

        board.positions[0] = character1
        board.positions[3] = character2
        board.positions[5] = character3

        assertEquals(
            Distribution.from(character1 to 1.0 / 3, character2 to 1.0 / 3, character3 to 1.0 / 3),
            board.characterDistribution(),
        )
        assertEquals(
            Distribution.from(
                character1 to 0.5,
                character2 to 0.5,
            ),
            board.frontRowCharacterDistribution(),
        )
        assertEquals(Distribution.from(character3 to 1.0), board.backRowCharacterDistribution())
    }
}

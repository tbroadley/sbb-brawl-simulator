package com.sbb

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BoardTest {
    @Test
    fun `next attacker is correctly calculated when the attacker survives`() {
        val board = Board()

        val attacker = Character.TINY.toInstance(board)
        val nextAttacker = Character.TINY.toInstance(board)

        board.positions[0] = attacker
        board.positions[1] = nextAttacker

        board.updateNextAttackerIndex(attacker)
        assertEquals(nextAttacker, board.nextAttacker())
        assertEquals(1, board.nextAttackerIndex)
    }

    @Test
    fun `next attacker is correctly calculated when the attacker dies`() {
        val board = Board()

        val attacker = Character.TINY.toInstance(board)
        val nextAttacker = Character.TINY.toInstance(board)

        board.positions[1] = nextAttacker

        board.updateNextAttackerIndex(attacker)
        assertEquals(nextAttacker, board.nextAttacker())
        assertEquals(1, board.nextAttackerIndex)
    }

    @Test
    fun `next attacker is correctly calculated when another unit replaces the dead attacker`() {
        val board = Board()

        val attacker = Character.TINY.toInstance(board)
        val replacement = Character.B_A_A_D_BILLY_GRUFF.toInstance(board)
        val nextAttacker = Character.TINY.toInstance(board)

        board.positions[0] = replacement
        board.positions[1] = nextAttacker

        board.updateNextAttackerIndex(attacker)
        assertEquals(replacement, board.nextAttacker())
        assertEquals(0, board.nextAttackerIndex)
    }

    @Test
    fun `next attacker is calculated correctly when there is a gap`() {
        val board = Board()

        val firstAttacker = Character.TINY.toInstance(board)
        val secondAttacker = Character.B_A_A_D_BILLY_GRUFF.toInstance(board)
        val thirdAttacker = Character.TINY.toInstance(board)

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
    fun `correct character is removed`() {
        val board = Board()

        val character1 = Character.TINY.toInstance(board)
        val character2 = Character.TINY.toInstance(board)

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
}

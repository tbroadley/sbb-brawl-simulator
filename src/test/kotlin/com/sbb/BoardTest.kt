package com.sbb

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BoardTest {
    @Test
    fun `next attacker is correctly calculated when the attacker survives`() {
        val attacker = Character.TINY.toInstance()
        val nextAttacker = Character.TINY.toInstance()

        val board = Board(attacker, nextAttacker)

        board.updateNextAttackerIndex(attacker)
        assertEquals(nextAttacker, board.nextAttacker())
        assertEquals(1, board.nextAttackerIndex)
    }

    @Test
    fun `next attacker is correctly calculated when the attacker dies`() {
        val attacker = Character.TINY.toInstance()
        val nextAttacker = Character.TINY.toInstance()

        val board = Board(null, nextAttacker)

        board.updateNextAttackerIndex(attacker)
        assertEquals(nextAttacker, board.nextAttacker())
        assertEquals(1, board.nextAttackerIndex)
    }

    @Test
    fun `next attacker is correctly calculated when another unit replaces the dead attacker`() {
        val attacker = Character.TINY.toInstance()
        val replacement = Character.B_A_A_D_BILLY_GRUFF.toInstance()
        val nextAttacker = Character.TINY.toInstance()

        val board = Board(replacement, nextAttacker)

        board.updateNextAttackerIndex(attacker)
        assertEquals(replacement, board.nextAttacker())
        assertEquals(0, board.nextAttackerIndex)
    }

    @Test
    fun `next attacker is calculated correctly when there is a gap`() {
        val firstAttacker = Character.TINY.toInstance()
        val secondAttacker = Character.B_A_A_D_BILLY_GRUFF.toInstance()
        val thirdAttacker = Character.TINY.toInstance()

        val board = Board(
            firstAttacker,
            null,
            secondAttacker,
            null,
            null,
            null,
            thirdAttacker,
        )

        board.updateNextAttackerIndex(firstAttacker)
        assertEquals(secondAttacker, board.nextAttacker())
        assertEquals(2, board.nextAttackerIndex)

        board.updateNextAttackerIndex(secondAttacker)
        assertEquals(thirdAttacker, board.nextAttacker())
        assertEquals(6, board.nextAttackerIndex)
    }
}

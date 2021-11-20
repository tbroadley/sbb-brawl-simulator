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
        assertEquals(board.nextAttacker(), nextAttacker)
        assertEquals(1, board.nextAttackerIndex)
    }
}

package com.sbb

import com.sbb.characters.Character.*
import com.sbb.characters.toInstance
import com.sbb.heroes.Hero.APOCALYPSE
import com.sbb.probability.Distribution
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BoardTest {
    @Test
    fun `next attacker is correctly calculated when the attacker survives`() {
        val board = Board(APOCALYPSE)

        val attacker = TINY.toInstance(board)
        val nextAttacker = TINY.toInstance(board)

        board.setStartingPositions(0 to attacker, 1 to nextAttacker)

        board.updateNextAttackerIndex(attacker)
        assertEquals(nextAttacker, board.nextAttacker())
        assertEquals(1, board.nextAttackerIndex)
    }

    @Test
    fun `next attacker is correctly calculated when the attacker dies`() {
        val board = Board(APOCALYPSE)

        val attacker = TINY.toInstance(board)
        val nextAttacker = TINY.toInstance(board)

        board.setStartingPositions(1 to nextAttacker)

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

        board.setStartingPositions(0 to replacement, 1 to nextAttacker)

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

        board.setStartingPositions(0 to firstAttacker, 2 to secondAttacker, 6 to thirdAttacker)

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

        board.setStartingPositions(0 to firstAttacker, 1 to babyRoot, 2 to secondAttacker)

        board.updateNextAttackerIndex(firstAttacker)
        assertEquals(secondAttacker, board.nextAttacker())
        assertEquals(2, board.nextAttackerIndex)
    }

    @Test
    fun `correct character is removed`() {
        val board = Board(APOCALYPSE)

        val character1 = TINY.toInstance(board)
        val character2 = TINY.toInstance(board)

        board.setStartingPositions(0 to character1, 2 to character2)

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

        board.setStartingPositions(0 to character1, 2 to character2)

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

        board.setStartingPositions(
            4 to character1,
            5 to character2,
            6 to character3,
        )

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

        board.setStartingPositions(
            0 to character1,
            3 to character2,
            5 to character3,
        )

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

    @Test
    fun `summoning and removing a unit adds and  its supports`() {
        val board = Board(APOCALYPSE)

        val babyRoot = BABY_ROOT.toInstance(board)

        board.setStartingPositions(
            0 to TINY.toInstance(board),
            1 to B_A_A_D_BILLY_GRUFF.toInstance(board),
            2 to TINY.toInstance(board),
        )

        board.summon(babyRoot, 4)

        with(board.positions[0]!!) {
            assertEquals(6, attack)
            assertEquals(4, health)
        }
        with(board.positions[1]!!) {
            assertEquals(2, attack)
            assertEquals(6, health)
        }
        with(board.positions[2]!!) {
            assertEquals(6, attack)
            assertEquals(1, health)
        }
        with(board.positions[4]!!) {
            assertEquals(0, attack)
            assertEquals(3, health)
        }

        board.remove(babyRoot)

        with(board.positions[0]!!) {
            assertEquals(6, attack)
            assertEquals(1, health)
        }
        with(board.positions[1]!!) {
            assertEquals(2, attack)
            assertEquals(3, health)
        }
        with(board.positions[2]!!) {
            assertEquals(6, attack)
            assertEquals(1, health)
        }
        assertNull(board.positions[4])
    }

    @Test
    fun `adding and removing a unit with trait-based supports adds and removes them from matching units`() {
        val board = Board(APOCALYPSE)

        val fanny = FANNY.toInstance(board)

        board.setStartingPositions(
            0 to TINY.toInstance(board),
            1 to B_A_A_D_BILLY_GRUFF.toInstance(board),
        )

        board.summon(fanny, 4)

        with(board.positions[0]!!) {
            assertEquals(8, attack)
            assertEquals(3, health)
        }
        with(board.positions[1]!!) {
            assertEquals(2, attack)
            assertEquals(3, health)
        }
        with(board.positions[4]!!) {
            assertEquals(2, attack)
            assertEquals(2, health)
        }

        board.remove(fanny)

        with(board.positions[0]!!) {
            assertEquals(6, attack)
            assertEquals(1, health)
        }
        with(board.positions[1]!!) {
            assertEquals(2, attack)
            assertEquals(3, health)
        }
        assertNull(board.positions[4])
    }

    @Test
    fun `empty board has no attackers`() {
        assertTrue(Board(APOCALYPSE).hasNoAttackers())
    }

    @Test
    fun `board with only zero-attack units has no attackers`() {
        val board = Board(APOCALYPSE)
        board.summon(BABY_ROOT.toInstance(board), 0)
        assertTrue(board.hasNoAttackers())
    }

    @Test
    fun `board with non-zero-attack units has attackers`() {
        val board = Board(APOCALYPSE)
        board.summon(BABY_ROOT.toInstance(board), 0)
        board.summon(TINY.toInstance(board), 1)
        assertFalse(board.hasNoAttackers())
    }
}

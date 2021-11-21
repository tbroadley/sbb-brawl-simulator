package com.sbb.probability

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class DistributionTest {
    @Test
    fun `can't create a distribution where probabilities don't sum 100 percent`() {
        assertThrows<IllegalArgumentException> {
            Distribution.from("A" to 0.5, "B" to 0.49)
        }
    }
}

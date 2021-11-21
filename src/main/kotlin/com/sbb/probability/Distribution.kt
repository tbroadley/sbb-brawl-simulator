package com.sbb.probability

import java.lang.IllegalStateException
import kotlin.random.Random

data class Distribution<T> private constructor(
    private val probabilitiesByOutcome: Map<T, Double>
) {
    override fun toString(): String {
        return "Distribution(probabilitiesByOutcome=$probabilitiesByOutcome)"
    }

    fun sample(): T {
        val sample = Random.nextDouble(1.0)
        var cumulativeProbability = 0.0

        for ((outcome, probability) in probabilitiesByOutcome) {
            cumulativeProbability += probability
            if (sample < cumulativeProbability) return outcome
        }

        throw IllegalStateException("Failed to sample $this")
    }

    companion object {
        fun <T> from(vararg probabilitiesByOutcome: Pair<T, Double>) = from(probabilitiesByOutcome.toMap())

        fun <T> from(probabilitiesByOutcome: Map<T, Double>): Distribution<T> {
            require(probabilitiesByOutcome.values.sum() == 1.0) {
                "Probabilities in $probabilitiesByOutcome don't sum to 1"
            }
            return Distribution(probabilitiesByOutcome)
        }
    }
}

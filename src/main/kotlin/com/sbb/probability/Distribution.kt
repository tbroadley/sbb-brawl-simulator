package com.sbb.probability

import java.lang.IllegalStateException
import kotlin.random.Random

data class Distribution<T>(
    val probabilitiesByOutcome: Map<T, Double>
) {
    constructor(vararg outcomesByProbability: Pair<T, Double>): this(outcomesByProbability.toMap())

    fun sample(): T {
        val sample = Random.nextDouble(1.0)
        var cumulativeProbability = 0.0

        for ((outcome, probability) in probabilitiesByOutcome) {
            cumulativeProbability += probability
            if (sample < cumulativeProbability) return outcome
        }

        throw IllegalStateException("Failed to sample $this")
    }
}

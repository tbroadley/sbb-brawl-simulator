package com.sbb.heroes

enum class Hero(
    val humanReadableName: String,
    val baseHealth: Long,
) {
    APOCALYPSE(
        humanReadableName = "Apocalypse",
        baseHealth = 50,
    ),
    SIR_GALAHAD(
        humanReadableName = "Sir Galahad",
        baseHealth = 40,
    ),
}

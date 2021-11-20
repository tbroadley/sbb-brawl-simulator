package com.sbb

enum class Character(
    val humanReadableName: String,
    val baseAttack: Long,
    val baseHealth: Long,
) {
    B_A_A_D_BILLY_GRUFF(
        humanReadableName = "B-a-a-d Billy Gruff",
        baseAttack = 2,
        baseHealth = 3,
    ),
    TINY(
        humanReadableName = "Tiny",
        baseAttack = 6,
        baseHealth = 1,
    )
}

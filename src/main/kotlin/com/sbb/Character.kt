package com.sbb

import com.sbb.Keyword.FLYING

enum class Character(
    val humanReadableName: String,
    val baseAttack: Long,
    val baseHealth: Long,
    val keywords: List<Keyword> = listOf(),
) {
    B_A_A_D_BILLY_GRUFF(
        humanReadableName = "B-a-a-d Billy Gruff",
        baseAttack = 2,
        baseHealth = 3,
    ),
    BABY_DRAGON(
        humanReadableName = "Baby Dragon",
        baseAttack = 3,
        baseHealth = 2,
        keywords = listOf(FLYING),
    ),
    TINY(
        humanReadableName = "Tiny",
        baseAttack = 6,
        baseHealth = 1,
    ),
}

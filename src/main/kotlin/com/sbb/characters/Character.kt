package com.sbb.characters

import com.sbb.characters.Keyword.FLYING
import com.sbb.characters.Keyword.SUPPORT

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
    BABY_ROOT(
        humanReadableName = "Baby Root",
        baseAttack = 0,
        baseHealth = 3,
        keywords = listOf(SUPPORT(health = 3)),
    ),
    TINY(
        humanReadableName = "Tiny",
        baseAttack = 6,
        baseHealth = 1,
    ),
    ;

    fun support(): SUPPORT? {
        return keywords.singleOrNull { it is SUPPORT } as SUPPORT?
    }
}

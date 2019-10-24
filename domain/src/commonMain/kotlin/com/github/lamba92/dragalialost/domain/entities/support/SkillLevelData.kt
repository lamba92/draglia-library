package com.github.lamba92.dragalialost.domain.entities.support

import com.github.lamba92.dragalialost.domain.entities.enums.SkillLevel

data class SkillLevelData(
    val level: SkillLevel,
    val might: Int,
    val description: String
) {
    override fun toString() =
        "Might $might | $description"
}
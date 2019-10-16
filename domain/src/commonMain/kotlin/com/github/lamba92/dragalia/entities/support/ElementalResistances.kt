package com.github.lamba92.dragalia.entities.support

import com.github.lamba92.dragalia.entities.enums.Element
import com.github.lamba92.dragalia.entities.enums.Element.*

data class ElementalResistances(
    val fire: Int,
    val water: Int,
    val wind: Int,
    val light: Int,
    val shadow: Int
) {
    operator fun get(element: Element) = when (element) {
        FIRE -> fire
        WATER -> water
        WIND -> wind
        LIGHT -> light
        SHADOW -> shadow
    }
}

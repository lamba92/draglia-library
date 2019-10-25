package com.github.lamba92.dragalialost.domain.repositories.queries

import com.github.lamba92.dragalialost.domain.entities.WyrmprintEntity
import com.github.lamba92.dragalialost.domain.entities.enums.AbilityType
import com.github.lamba92.dragalialost.domain.entities.enums.Affliction
import com.github.lamba92.dragalialost.domain.entities.enums.Element
import com.github.lamba92.dragalialost.domain.entities.enums.Rarity

data class WyrmprintsQuery(
    val name: String?,
    val rarities: Set<Rarity>,
    val afflictionResistances: Set<Affliction>,
    val elementalResistances: Set<Element>,
    val abilityTypes: Set<AbilityType>
) {
    operator fun contains(entity: WyrmprintEntity) =
        (if (rarities.isNotEmpty()) entity.baseRarity in rarities else true) &&
                (if (abilityTypes.isNotEmpty()) entity.abilityTypes.any { it in abilityTypes } else true) &&
                (if (elementalResistances.isNotEmpty()) entity.elementalResistances.all {
                    it in elementalResistances
                } else true) &&
                (if (afflictionResistances.isNotEmpty()) entity.afflictionResistances.all {
                    it in afflictionResistances
                } else true) &&
                name?.let { it.toLowerCase() in entity.name.toLowerCase() } ?: true

}
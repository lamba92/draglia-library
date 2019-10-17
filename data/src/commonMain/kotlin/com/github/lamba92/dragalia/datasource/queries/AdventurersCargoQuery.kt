package com.github.lamba92.dragalia.datasource.queries

class AdventurersCargoQuery(
    val weaponType: String? = null,
    val heroClass: String? = null,
    element: String? = null,
    rarity: Int? = null,
    name: String? = null
) : WithElementAndRarityCargoQuery(element, rarity, name)
package com.github.lamba92.dragalialost.data.datasource.queries

class WyrmprintsCargoQuery(
    val type: String? = null,
    element: String? = null,
    rarity: String? = null,
    name: String? = null
) : WithElementAndRarityCargoQuery(element, rarity, name)
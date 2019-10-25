package com.github.lamba92.dragalialost.domain.repositories

import com.github.lamba92.dragalialost.domain.repositories.queries.AdventurersQueryBuilder
import com.github.lamba92.dragalialost.domain.repositories.queries.DragonsQueryBuilder
import com.github.lamba92.dragalialost.domain.repositories.queries.WyrmprintsQueryBuilder

fun DragaliaLostRepository.searchAdventurers(
    limit: Int = 500,
    queryBuilder: AdventurersQueryBuilder.() -> Unit
) = searchAdventurers(AdventurersQueryBuilder().apply(queryBuilder).toQuery(), limit)

fun DragaliaLostRepository.searchDragons(
    limit: Int = 500, queryBuilder: DragonsQueryBuilder.() -> Unit
) = searchDragons(DragonsQueryBuilder().apply(queryBuilder).toQuery(), limit)

fun DragaliaLostRepository.searchWyrmprints(limit: Int = 500, queryBuilder: WyrmprintsQueryBuilder.() -> Unit) =
    searchWyrmprints(WyrmprintsQueryBuilder().apply(queryBuilder).toQuery(), limit)

fun DragaliaLostRepository.searchAllAdventurers() =
    searchAdventurers {}

fun DragaliaLostRepository.searchAllDragons() =
    searchDragons {}

fun DragaliaLostRepository.searchAllWyrmprints() =
    searchWyrmprints {}

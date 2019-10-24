package com.github.lamba92.dragalialost.data.repositories

import com.github.lamba92.dragalialost.data.datasource.GamepediaDatasource
import com.github.lamba92.dragalialost.data.datasource.GamepediaDatasourceCache
import com.github.lamba92.dragalialost.data.mappers.*
import com.github.lamba92.dragalialost.data.utils.*
import com.github.lamba92.dragalialost.domain.repositories.DragaliaLostRepository
import com.github.lamba92.dragalialost.domain.repositories.queries.AdventurersQueryBuilder
import com.github.lamba92.dragalialost.domain.repositories.queries.DragonsQueryBuilder
import com.github.lamba92.dragalialost.domain.repositories.queries.WyrmprintsQueryBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet

class DragaliaLostRepositoryImplementation(
    private val datasource: GamepediaDatasource,
    private val cache: GamepediaDatasourceCache,
    private val adventurersQueryMapper: AdventurersQueryMapper,
    private val wyrmprintsQueryMapper: WyrmprintsQueryMapper,
    private val dragonsQueryMapper: DragonsQueryMapper,
    private val dragonsMapper: DragonsMapper,
    private val adventurerMapper: AdventurerMapper,
    private val wyrmprintsMapper: WyrmprintsMapper
) : DragaliaLostRepository {

    @ExperimentalCoroutinesApi
    @FlowPreview
    override suspend fun searchAdventurers(query: AdventurersQueryBuilder, limit: Int) =
        adventurersQueryMapper.toRemote(query)
            .asFlow()
            .map { dsQuery ->
                cache.searchAdventurerIds(dsQuery, limit) ?: datasource.searchAdventurerIds(dsQuery, limit).also {
                    cache.cacheAdventurerCargoQuery(dsQuery, limit, it)
                }
            }
            .flattenConcat()
            .toSet()
            .asFlow()
            .map { (id, variationId) ->
                cache.getAdventurerByIds(id, variationId) ?: datasource.getAdventurerByIds(id, variationId)
                    .also { cache.cacheAdventurerByIds(id, variationId, it) }
            }
            .scopedMap { json ->
                with(json) {
                    val a11 = async { getAbilityData(Abilities11) }
                    val a12 = async { getAbilityData(Abilities12) }
                    val a13 = Abilities13.ifIsNotBlankOrZero { async { getAbilityData(it) } }

                    val a21 = async { getAbilityData(Abilities21) }
                    val a22 = async { getAbilityData(Abilities22) }
                    val a23 = Abilities23.ifIsNotBlankOrZero { async { getAbilityData(it) } }

                    val a31 = async { getAbilityData(Abilities31) }
                    val a32 = Abilities32.ifIsNotBlankOrZero { async { getAbilityData(it) } }
                    val a33 = Abilities33.ifIsNotBlankOrZero { async { getAbilityData(it) } }

                    val s1 = async { getSkillData(Skill1Name) }
                    val s2 = async { getSkillData(Skill2Name) }

                    val coa1 = async { getCoAbilityData(ExAbilityData1) }
                    val coa2 = async { getCoAbilityData(ExAbilityData2) }
                    val coa3 = async { getCoAbilityData(ExAbilityData3) }
                    val coa4 = async { getCoAbilityData(ExAbilityData4) }
                    val coa5 = async { getCoAbilityData(ExAbilityData5) }

                    val images = (Rarity.toInt()..5).map {
                        async { getAndCacheAdventurerPortraitImageInfoByIds(Id, VariationId, it) }
                    }
                    val icons = (Rarity.toInt()..5).map {
                        async { getAndCacheAdventurerIconImageInfoByIds(Id, VariationId, it) }
                    }

                    AdventurerMapper.Params(
                        json, a11.await(), a12.await(), a13?.await(), a21.await(), a22.await(), a23?.await(),
                        a31.await(), a32?.await(), a33?.await(), coa1.await(), coa2.await(), coa3.await(),
                        coa4.await(), coa5.await(), s1.await(), s2.await(), images.awaitAll(),
                        icons.awaitAll()
                    )
                }
            }
            .map { adventurerMapper(it) }
            .catch {
                println("An adventurer errored: $it")
            }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override suspend fun searchDragons(query: DragonsQueryBuilder, limit: Int) =
        dragonsQueryMapper.toRemote(query)
            .asFlow()
            .map { dsQuery ->
                cache.searchDragonIds(dsQuery, limit) ?: datasource.searchDragonIds(dsQuery, limit).also {
                    cache.cacheDragonCargoQuery(dsQuery, limit, it)
                }
            }
            .flattenConcat()
            .toSet()
            .asFlow()
            .map { dragonId ->
                cache.getDragonById(dragonId) ?: datasource.getDragonById(dragonId)
                    .also { cache.cacheDragonById(dragonId, it) }
            }
            .scopedMap { json ->
                with(json) {
                    val a11 = async { getAbilityData(Abilities11) }
                    val a12 = async { getAbilityData(Abilities12) }

                    val a21 = Abilities21.ifIsNotBlankOrZero { async { getAbilityData(it) } }
                    val a22 = Abilities22.ifIsNotBlankOrZero { async { getAbilityData(it) } }

                    val s1 = async { getSkillData(SkillName) }

                    val icon = async { getAndCacheDragonIconImageInfoById(BaseId) }
                    val portrait = async { getAndCacheDragonPortraitImageInfoById(BaseId) }

                    DragonsMapper.Params(
                        json, a11.await(), a12.await(), a21?.await(), a22?.await(),
                        s1.await(), icon.await(), portrait.await()
                    )
                }
            }
            .catch {
                println("A dragon errored: $it")
            }
            .map { dragonsMapper(it) }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override suspend fun searchWyrmprints(query: WyrmprintsQueryBuilder, limit: Int) =
        wyrmprintsQueryMapper.toRemote(query)
            .asFlow()
            .map { dsQuery ->
                cache.searchWyrmprintIds(dsQuery, limit) ?: datasource.searchWyrmprintIds(dsQuery, limit).also {
                    cache.cacheWyrmprintCargoQuery(dsQuery, limit, it)
                }
            }
            .flattenConcat()
            .toSet()
            .asFlow()
            .map { id ->
                cache.getWyrmprintById(id) ?: datasource.getWyrmprintById(id)
                    .also { cache.cacheWyrmprintById(id, it) }
            }
            .scopedMap { json ->
                with(json) {
                    val a11 = async { getAbilityData(Abilities11) }
                    val a12 = async { getAbilityData(Abilities12) }
                    val a13 = async { getAbilityData(Abilities13) }

                    val a21 = Abilities21.ifIsNotBlankOrZero { async { getAbilityData(it) } }
                    val a22 = Abilities22.ifIsNotBlankOrZero { async { getAbilityData(it) } }
                    val a23 = Abilities23.ifIsNotBlankOrZero { async { getAbilityData(it) } }

                    val a31 = Abilities31.ifIsNotBlankOrZero { async { getAbilityData(it) } }
                    val a32 = Abilities32.ifIsNotBlankOrZero { async { getAbilityData(it) } }
                    val a33 = Abilities33.ifIsNotBlankOrZero { async { getAbilityData(it) } }

                    val icon1 = async { getAndCacheWyrmprintIconImageInfoById(Id, 1) }
                    val icon2 = async { getAndCacheWyrmprintIconImageInfoById(Id, 2) }
                    val artwork1 = async { getAndCacheWyrmprintIconPortraitInfoById(Id, 1) }
                    val artwork2 = async { getAndCacheWyrmprintIconPortraitInfoById(Id, 2) }

                    WyrmprintsMapper.Params(
                        json, a11.await(), a12.await(), a13.await(), a21?.await(), a22?.await(),
                        a23?.await(), a31?.await(), a32?.await(), a33?.await(), icon1.await(),
                        icon2.await(), artwork1.await(), artwork2.await()
                    )
                }
            }
            .catch {
                println("A wyrmprint errored: $it")
            }
            .map { wyrmprintsMapper(it) }

    private suspend fun getAbilityData(id: String) =
        getAndCacheAbilityById(id) with { getAndCacheAbilityIconImageInfoByFileName(AbilityIconName) }

    private suspend fun getCoAbilityData(id: String) =
        getAndCacheCoAbilityById(id).with { getAndCacheCoAbilityIconImageInfoByFileName(AbilityIconName) }

    private suspend fun getSkillData(id: String) = coroutineScope {
        getAndCacheSkillByName(id) with {
            Triple(
                async { getAndCacheSkillIconImageInfoByFileName(SkillLv1IconName) },
                async { getAndCacheSkillIconImageInfoByFileName(SkillLv2IconName) },
                async { getAndCacheSkillIconImageInfoByFileName(SkillLv3IconName) }
            ).await()
        }
    }

    private suspend fun getAndCacheAbilityById(abilityId: String) = cache.getAbilityById(abilityId)
        ?: datasource.getAbilityById(abilityId).also { cache.cacheAbilityById(abilityId, it) }

    private suspend fun getAndCacheSkillByName(skillId: String) = cache.getSkillByName(skillId)
        ?: datasource.getSkillByName(skillId).also { cache.cacheSkillByName(skillId, it) }

    private suspend fun getAndCacheCoAbilityById(coAbilityId: String) = cache.getCoAbilityById(coAbilityId)
        ?: datasource.getCoAbilityById(coAbilityId).also { cache.cacheCoAbilityById(coAbilityId, it) }

    private suspend fun getAndCacheAbilityIconImageInfoByFileName(fileName: String) =
        cache.getAbilityIconByFileName(fileName) ?: datasource.getAbilityIconByFileName(fileName)
            .also { cache.cacheAbilityIconByFileName(fileName, it) }

    private suspend fun getAndCacheCoAbilityIconImageInfoByFileName(fileName: String) =
        cache.getCoAbilityIconByFileName(fileName) ?: datasource.getCoAbilityIconByFileName(fileName)
            .also { cache.cacheCoAbilityIconByFileName(fileName, it) }

    private suspend fun getAndCacheSkillIconImageInfoByFileName(fileName: String) =
        cache.getSkillIconByIconName(fileName) ?: datasource.getSkillIconByIconName(fileName)
            .also { cache.cacheSkillIconByIconName(fileName, it) }

    private suspend fun getAndCacheAdventurerPortraitImageInfoByIds(id: String, variationId: String, rarity: Int) =
        cache.getAdventurerPortraitById(id, variationId, rarity)
            ?: datasource.getAdventurerPortraitById(id, variationId, rarity)
                .also { cache.cacheAdventurerPortraitById(id, variationId, rarity, it) }

    private suspend fun getAndCacheAdventurerIconImageInfoByIds(id: String, variationId: String, rarity: Int) =
        cache.getAdventurerIconById(id, variationId, rarity)
            ?: datasource.getAdventurerIconById(id, variationId, rarity)
                .also { cache.cacheAdventurerIconById(id, variationId, rarity, it) }

    private suspend fun getAndCacheDragonPortraitImageInfoById(id: String) =
        cache.getDragonPortraitById(id) ?: datasource.getDragonPortraitById(id)
            .also { cache.cacheDragonPortraitById(id, it) }

    private suspend fun getAndCacheDragonIconImageInfoById(id: String) =
        cache.getDragonIconById(id) ?: datasource.getDragonIconById(id)
            .also { cache.cacheDragonIconById(id, it) }

    private suspend fun getAndCacheWyrmprintIconImageInfoById(id: String, vestige: Int) =
        cache.getWyrmprintIconByIds(id, vestige) ?: datasource.getWyrmprintIconByIds(id, vestige)
            .also { cache.cacheWyrmprintIconByIds(id, vestige, it) }

    private suspend fun getAndCacheWyrmprintIconPortraitInfoById(id: String, vestige: Int) =
        cache.getWyrmprintPortraitByIds(id, vestige) ?: datasource.getWyrmprintPortraitByIds(id, vestige)
            .also { cache.cacheWyrmprintPortraitByIds(id, vestige, it) }

}

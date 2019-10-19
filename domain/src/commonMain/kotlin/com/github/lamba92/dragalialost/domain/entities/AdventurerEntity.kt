package com.github.lamba92.dragalialost.domain.entities

import com.github.lamba92.dragalialost.domain.entities.enums.*
import com.github.lamba92.dragalialost.domain.entities.support.*
import com.soywiz.klock.DateTime

data class AdventurerEntity(
    override val name: String,
    val description: String,
    override val hp: Int,
    override val strength: Int,
    override val maxLevel: Int,
    val bonusHp: ManaCircleBonusStats,
    val bonusStrength: ManaCircleBonusStats,
    override val baseMinMight: Int,
    override val baseMaxMight: Int,
    val defense: Int,
    val heroClass: HeroCLass,
    val gender: Gender,
    val race: Race,
    override val rarity: Rarity,
    val voiceActorEN: VoiceActor,
    val voiceActorJP: VoiceActor,
    override val obtainedFrom: Source,
    override val releaseDate: DateTime,
    override val availability: Availability,
    override val artwork: String,
    val element: Element,
    val weaponType: WeaponType,
    val skill1: Skill,
    val skill2: Skill,
    val ability: Ability,
    val ability2: Ability?,
    val ability3: Ability?,
    val coAbility: CoAbility
) : DragaliaEntity {
    companion object {
        const val FORCE_STRIKE_LVL2_MIGHT = 120
        const val MAX_LVL = 80
    }
}
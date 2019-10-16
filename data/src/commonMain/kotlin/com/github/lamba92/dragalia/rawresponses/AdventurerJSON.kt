package com.github.lamba92.dragalia.rawresponses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdventurerJSON(
    val Abilities11: String,
    val Abilities12: String,
    val Abilities13: String,
    val Abilities14: String,
    val Abilities21: String,
    val Abilities22: String,
    val Abilities23: String,
    val Abilities24: String,
    val Abilities31: String,
    val Abilities32: String,
    val Abilities33: String,
    val Abilities34: String,
    val Availability: String,
    val CharaType: String,
    val DefCoef: String,
    val Description: String,
    val ElementalType: String,
    val ElementalTypeId: String,
    val EnglishCV: String,
    val ExAbilityData1: String,
    val ExAbilityData2: String,
    val ExAbilityData3: String,
    val ExAbilityData4: String,
    val ExAbilityData5: String,
    val FullName: String,
    val Gender: String,
    val Id: String,
    val IdLong: String,
    val IsPlayable: String,
    val JapaneseCV: String,
    val ManaCircleName: String,
    val MaxAtk: String,
    val MaxFriendshipPoint: String,
    val MaxHp: String,
    val McFullBonusAtk5: String,
    val McFullBonusHp5: String,
    val MinAtk3: String,
    val MinAtk4: String,
    val MinAtk5: String,
    val MinDef: String,
    val MinHp3: String,
    val MinHp4: String,
    val MinHp5: String,
    val Name: String,
    val NameJP: String,
    val NameSC: String,
    val NameTC: String,
    val Obtain: String,
    val PlusAtk0: String,
    val PlusAtk1: String,
    val PlusAtk2: String,
    val PlusAtk3: String,
    val PlusAtk4: String,
    val PlusHp0: String,
    val PlusHp1: String,
    val PlusHp2: String,
    val PlusHp3: String,
    val PlusHp4: String,
    val Race: String,
    val Rarity: String,
    val ReleaseDate: String,
    val ReleaseDate__precision: String? = null,
    @SerialName("ReleaseDate  precision") val ReleaseDate__precision2: String? = null,
    val Skill1Name: String,
    val Skill2Name: String,
    val Title: String,
    val TitleJP: String,
    val TitleSC: String,
    val TitleTC: String,
    val VariationId: String,
    val WeaponType: String,
    val WeaponTypeId: String
) : CargoQueryable
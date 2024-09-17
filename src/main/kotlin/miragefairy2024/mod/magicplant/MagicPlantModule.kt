package miragefairy2024.mod.magicplant

import miragefairy2024.MirageFairy2024
import miragefairy2024.ModContext
import miragefairy2024.mod.magicplant.contents.initTraitCard
import miragefairy2024.mod.magicplant.contents.initTraitConditionCard
import miragefairy2024.mod.magicplant.contents.initTraitEffectKeyCard
import miragefairy2024.mod.magicplant.contents.magicplants.DiamondLuminariaCard
import miragefairy2024.mod.magicplant.contents.magicplants.EmeraldLuminariaCard
import miragefairy2024.mod.magicplant.contents.magicplants.MirageFlowerCard
import miragefairy2024.mod.magicplant.contents.magicplants.PhantomFlowerCard
import miragefairy2024.mod.magicplant.contents.magicplants.VeropedaCard
import miragefairy2024.util.Translation
import miragefairy2024.util.enJa

val magicPlantCards: List<MagicPlantCard<*>> = listOf(
    MirageFlowerCard,
    PhantomFlowerCard,
    VeropedaCard,
    DiamondLuminariaCard,
    EmeraldLuminariaCard,
)

val TRAIT_TRANSLATION = Translation({ "item.${MirageFairy2024.MOD_ID}.magic_plant.trait" }, "Trait", "特性")
val CREATIVE_ONLY_TRANSLATION = Translation({ "item.${MirageFairy2024.MOD_ID}.magic_plant.creativeOnly" }, "Creative Only", "クリエイティブ専用")
val GUI_TRANSLATION = Translation({ "item.${MirageFairy2024.MOD_ID}.magic_plant.gui" }, "Use while sneaking to show traits", "スニーク中に使用時、特性GUIを表示")
val INVALID_TRANSLATION = Translation({ "item.${MirageFairy2024.MOD_ID}.magic_plant.invalid" }, "Invalid", "無効")

context(ModContext)
fun initMagicPlantModule() {

    TRAIT_TRANSLATION.enJa()
    CREATIVE_ONLY_TRANSLATION.enJa()
    GUI_TRANSLATION.enJa()
    INVALID_TRANSLATION.enJa()


    initTraitSpawnConditionScope()
    initTraitListScreenHandler()
    initTraitConditionCard()
    initTraitEffectKeyCard()
    initTraitCard()
    initCreativeGeneAmpoule()
    initSeedBag()

    magicPlantCards.forEach { card ->
        card.init()
    }

}

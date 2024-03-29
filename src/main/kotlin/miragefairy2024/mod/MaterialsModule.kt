package miragefairy2024.mod

import miragefairy2024.MirageFairy2024
import miragefairy2024.mod.fairy.RandomFairySummoningItem
import miragefairy2024.util.Translation
import miragefairy2024.util.enJa
import miragefairy2024.util.from
import miragefairy2024.util.modId
import miragefairy2024.util.on
import miragefairy2024.util.register
import miragefairy2024.util.registerComposterInput
import miragefairy2024.util.registerFuel
import miragefairy2024.util.registerGeneratedItemModelGeneration
import miragefairy2024.util.registerItemGroup
import miragefairy2024.util.registerShapedRecipeGeneration
import miragefairy2024.util.registerShapelessRecipeGeneration
import miragefairy2024.util.registerSmeltingRecipeGeneration
import miragefairy2024.util.registerTagGeneration
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import kotlin.math.pow

enum class MaterialCard(
    path: String,
    val enName: String,
    val jaName: String,
    val poemList: PoemList,
    val fuelValue: Int? = null,
    val foodComponent: FoodComponent? = null,
    val creator: (Item.Settings) -> Item = ::Item,
) {

    XARPITE(
        // TODO add purpose
        "xarpite", "Xarpite", "紅天石",
        PoemList(2).poem("Binds astral flux with magnetic force", "黒鉄の鎖は繋がれる。血腥い魂の檻へ。"),
        // TODO 使えるワード：牢獄
    ),
    MIRANAGITE(
        // TODO add purpose
        "miranagite", "Miranagite", "蒼天石",
        PoemList(2).poem("Astral body crystallized by anti-entropy", "秩序の叛乱、天地創造の逆光。"),
    ),

    MIRAGE_LEAVES(
        "mirage_leaves", "Mirage Leaves", "ミラージュの葉",
        PoemList(1).poem("Don't cut your fingers!", "刻まれる、記憶の破片。"),
    ),
    MIRAGE_STEM(
        "mirage_stem", "Mirage Stem", "ミラージュの茎",
        PoemList(1).poem("Cell wall composed of amorphous ether", "植物が手掛ける、分子レベルの硝子細工。"),
    ),
    VEROPEDA_LEAF(
        "veropeda_leaf", "Veropeda Leaf", "ヴェロペダの葉",
        PoemList(1).poem("Said to house the soul of a demon", "その身融かされるまでの快楽。"),
    ),
    VEROPEDA_BERRIES(
        "veropeda_berries", "Veropeda Berries", "ヴェロペダの実",
        PoemList(1)
            .poem("Has analgesic and stimulant effects", "悪魔の囁きを喰らう。")
            .description("Healing and rare nausea by eating", "食べると回復、まれに吐き気"),
        foodComponent = FoodComponent.Builder()
            .hunger(1)
            .saturationModifier(0.1F)
            .snack()
            .statusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 20 * 3), 1.0F)
            .statusEffect(StatusEffectInstance(StatusEffects.NAUSEA, 20 * 20), 0.01F)
            .build(),
    ),
    HAIMEVISKA_SAP(
        "haimeviska_sap", "Haimeviska Sap", "ハイメヴィスカの樹液",
        PoemList(1)
            .poem("Smooth and mellow on the palate", "口福のアナムネシス。")
            .description("Gain experience by eating", "食べると経験値を獲得"),
        fuelValue = 200,
        foodComponent = FoodComponent.Builder()
            .hunger(1)
            .saturationModifier(0.1F)
            .statusEffect(StatusEffectInstance(experienceStatusEffect, 20), 1.0F)
            .build(),
    ),
    FAIRY_PLASTIC(
        // TODO add purpose
        "fairy_plastic", "Fairy Plastic", "妖精のプラスチック",
        PoemList(2).poem("Thermoplastic organic polymer", "凍てつく記憶の宿る石。"),
    ),
    FAIRY_RUBBER(
        // TODO add purpose
        "fairy_rubber", "Fairy Rubber", "夜のかけら",
        PoemList(3).poem("Minimize the risk of losing belongings", "空は怯える夜精に一握りの温かい闇を与えた"),
    ),

    TINY_MIRAGE_FLOUR(
        "tiny_mirage_flour", "Tiny Pile of Mirage Flour", "小さなミラージュの花粉",
        PoemList(1).poem("Compose the body of Mirage fairy", "ささやかな温もりを、てのひらの上に。"),
        creator = { RandomFairySummoningItem(9.0.pow(-1.0), it) },
    ),
    MIRAGE_FLOUR(
        "mirage_flour", "Mirage Flour", "ミラージュの花粉",
        PoemList(1).poem("Containing metallic organic matter", "叡智の根源、創発のファンタジア。"),
        creator = { RandomFairySummoningItem(9.0.pow(0.0), it) },
    ),
    MIRAGE_FLOUR_OF_NATURE(
        "mirage_flour_of_nature", "Mirage Flour of Nature", "自然のミラージュの花粉",
        PoemList(1).poem("Use the difference in ether resistance", "艶やかなほたる色に煌めく鱗粉。"),
        creator = { RandomFairySummoningItem(9.0.pow(1.0), it) },
    ),
    MIRAGE_FLOUR_OF_EARTH(
        "mirage_flour_of_earth", "Mirage Flour of Earth", "大地のミラージュの花粉",
        PoemList(2).poem("As intelligent as humans", "黄金の魂が示す、好奇心の輝き。"),
        creator = { RandomFairySummoningItem(9.0.pow(2.0), it) },
    ),
    MIRAGE_FLOUR_OF_UNDERWORLD(
        "mirage_flour_of_underworld", "Mirage Flour of Underworld", "地底のミラージュの花粉",
        PoemList(2).poem("Awaken fairies in the world and below", "1,300ケルビンの夜景。"),
        creator = { RandomFairySummoningItem(9.0.pow(3.0), it) },
    ),
    MIRAGE_FLOUR_OF_SKY(
        "mirage_flour_of_sky", "Mirage Flour of Sky", "天空のミラージュの花粉",
        PoemList(3).poem("Explore atmosphere and nearby universe", "蒼淵を彷徨う影、導きの光。"),
        creator = { RandomFairySummoningItem(9.0.pow(4.0), it) },
    ),
    MIRAGE_FLOUR_OF_UNIVERSE(
        "mirage_flour_of_universe", "Mirage Flour of Universe", "宇宙のミラージュの花粉",
        PoemList(3)
            .poem("poem1", "Leap spaces by collapsing time crystals,", "運命の束、時の結晶、光速の呪いを退けよ、")
            .poem("poem2", "capture ether beyond observable universe", "讃えよ、アーカーシャに眠る自由の頂きを。"),
        creator = { RandomFairySummoningItem(9.0.pow(5.0), it) },
    ),
    MIRAGE_FLOUR_OF_TIME(
        "mirage_flour_of_time", "Mirage Flour of Time", "時空のミラージュの花粉",
        PoemList(4)
            .poem("poem1", "Attracts nearby parallel worlds outside", "虚空に眠る時の断片。因果の光が貫くとき、")
            .poem("poem2", "this universe and collects their ether.", "亡失の世界は探し始める。無慈悲な真実を。"),
        creator = { RandomFairySummoningItem(9.0.pow(6.0), it) },
    ),

    FRACTAL_WISP(
        "fractal_wisp", "Fractal Wisp", "フラクタルウィスプ",
        PoemList(1)
            .poem("poem1", "The fairy of the fairy of the fairy", "妖精の妖精の妖精の妖精の妖精の妖精の妖精")
            .poem("poem2", "of the fairy of the fairy of the f", "の妖精の妖精の妖精の妖精の妖精の妖精の妖"),
        // TODO 用途
    ),

    FAIRY_QUEST_CARD_BASE(
        "fairy_quest_card_base", "Fairy Quest Card Base", "フェアリークエストカードベース",
        PoemList(1).poem("Am I hopeful in the parallel world?", "存在したかもしれない僕たちのかたち。")
    ),

    ;

    val identifier = Identifier(MirageFairy2024.modId, path)
    val item = Item.Settings()
        .let { if (foodComponent != null) it.food(foodComponent) else it }
        .let { creator(it) }
}

val MIRAGE_FLOUR_TAG: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier(MirageFairy2024.modId, "mirage_flour"))
val WISP_TAG: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier(MirageFairy2024.modId, "wisp"))

val APPEARANCE_RATE_BONUS_TRANSLATION = Translation({ "item.miragefairy2024.mirage_flour.appearance_rate_bonus" }, "Appearance Rate Bonus", "出現率ボーナス")

fun initMaterialsModule() {

    MaterialCard.entries.forEach { card ->
        card.item.register(Registries.ITEM, card.identifier)
        card.item.registerItemGroup(mirageFairy2024ItemGroupCard.itemGroupKey)
        card.item.registerGeneratedItemModelGeneration()
        card.item.enJa(card.enName, card.jaName)
        card.item.registerPoem(card.poemList)
        card.item.registerPoemGeneration(card.poemList)
        if (card.fuelValue != null) card.item.registerFuel(card.fuelValue)
    }

    APPEARANCE_RATE_BONUS_TRANSLATION.enJa()

    fun registerCompressionRecipeGeneration(low: MaterialCard, high: MaterialCard) {
        registerShapedRecipeGeneration(high.item) {
            pattern("###")
            pattern("###")
            pattern("###")
            input('#', low.item)
        } on low.item from low.item
        registerShapelessRecipeGeneration(low.item, 9) {
            input(high.item)
        } on high.item from high.item
    }
    registerCompressionRecipeGeneration(MaterialCard.TINY_MIRAGE_FLOUR, MaterialCard.MIRAGE_FLOUR)
    registerCompressionRecipeGeneration(MaterialCard.MIRAGE_FLOUR, MaterialCard.MIRAGE_FLOUR_OF_NATURE)
    registerCompressionRecipeGeneration(MaterialCard.MIRAGE_FLOUR_OF_NATURE, MaterialCard.MIRAGE_FLOUR_OF_EARTH)
    registerCompressionRecipeGeneration(MaterialCard.MIRAGE_FLOUR_OF_EARTH, MaterialCard.MIRAGE_FLOUR_OF_UNDERWORLD)
    registerCompressionRecipeGeneration(MaterialCard.MIRAGE_FLOUR_OF_UNDERWORLD, MaterialCard.MIRAGE_FLOUR_OF_SKY)
    registerCompressionRecipeGeneration(MaterialCard.MIRAGE_FLOUR_OF_SKY, MaterialCard.MIRAGE_FLOUR_OF_UNIVERSE)
    registerCompressionRecipeGeneration(MaterialCard.MIRAGE_FLOUR_OF_UNIVERSE, MaterialCard.MIRAGE_FLOUR_OF_TIME)

    // ミラージュの葉
    MaterialCard.MIRAGE_LEAVES.item.registerComposterInput(0.5F)

    // ミラージュの茎
    registerShapelessRecipeGeneration(MaterialCard.MIRAGE_STEM.item) {
        input(MaterialCard.MIRAGE_LEAVES.item)
    } on MaterialCard.MIRAGE_LEAVES.item
    MaterialCard.MIRAGE_STEM.item.registerComposterInput(0.5F)
    registerShapedRecipeGeneration(Items.STICK, 2) {
        pattern("#")
        pattern("#")
        input('#', MaterialCard.MIRAGE_STEM.item)
    } on MaterialCard.MIRAGE_STEM.item modId MirageFairy2024.modId from MaterialCard.MIRAGE_STEM.item

    // ヴェロペダの葉
    MaterialCard.VEROPEDA_LEAF.item.registerComposterInput(0.5F)
    registerSmeltingRecipeGeneration(MaterialCard.VEROPEDA_LEAF.item, Items.IRON_NUGGET, 0.1) on MaterialCard.VEROPEDA_LEAF.item modId MirageFairy2024.modId from MaterialCard.VEROPEDA_LEAF.item

    // ヴェロペダの実
    MaterialCard.VEROPEDA_BERRIES.item.registerComposterInput(0.3F)

    // ハイメヴィスカの樹液→松明
    registerShapedRecipeGeneration(Items.TORCH) {
        pattern("#")
        pattern("S")
        input('#', MaterialCard.HAIMEVISKA_SAP.item)
        input('S', Items.STICK)
    } on MaterialCard.HAIMEVISKA_SAP.item modId MirageFairy2024.modId from MaterialCard.HAIMEVISKA_SAP.item

    // ミラージュの花粉
    MaterialCard.TINY_MIRAGE_FLOUR.item.registerTagGeneration { MIRAGE_FLOUR_TAG }
    MaterialCard.MIRAGE_FLOUR.item.registerTagGeneration { MIRAGE_FLOUR_TAG }
    MaterialCard.MIRAGE_FLOUR_OF_NATURE.item.registerTagGeneration { MIRAGE_FLOUR_TAG }
    MaterialCard.MIRAGE_FLOUR_OF_EARTH.item.registerTagGeneration { MIRAGE_FLOUR_TAG }
    MaterialCard.MIRAGE_FLOUR_OF_UNDERWORLD.item.registerTagGeneration { MIRAGE_FLOUR_TAG }
    MaterialCard.MIRAGE_FLOUR_OF_SKY.item.registerTagGeneration { MIRAGE_FLOUR_TAG }
    MaterialCard.MIRAGE_FLOUR_OF_UNIVERSE.item.registerTagGeneration { MIRAGE_FLOUR_TAG }
    MaterialCard.MIRAGE_FLOUR_OF_TIME.item.registerTagGeneration { MIRAGE_FLOUR_TAG }

    // フラクタルウィスプ
    MaterialCard.FRACTAL_WISP.item.registerTagGeneration { WISP_TAG }

}

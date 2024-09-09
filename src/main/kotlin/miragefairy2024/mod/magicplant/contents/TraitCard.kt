package miragefairy2024.mod.magicplant.contents

import miragefairy2024.MirageFairy2024
import miragefairy2024.ModContext
import miragefairy2024.mod.BiomeCards
import miragefairy2024.mod.FAIRY_BIOME_TAG
import miragefairy2024.mod.magicplant.MutableTraitEffects
import miragefairy2024.mod.magicplant.Trait
import miragefairy2024.mod.magicplant.TraitSpawnCondition
import miragefairy2024.mod.magicplant.TraitSpawnConditionScope
import miragefairy2024.mod.magicplant.TraitSpawnRarity
import miragefairy2024.mod.magicplant.TraitSpawnSpec
import miragefairy2024.mod.magicplant.anywhere
import miragefairy2024.mod.magicplant.enJa
import miragefairy2024.mod.magicplant.end
import miragefairy2024.mod.magicplant.nether
import miragefairy2024.mod.magicplant.overworld
import miragefairy2024.mod.magicplant.traitRegistry
import miragefairy2024.mod.magicplant.unaryPlus
import miragefairy2024.util.HumidityCategory
import miragefairy2024.util.TemperatureCategory
import miragefairy2024.util.Translation
import miragefairy2024.util.enJa
import miragefairy2024.util.invoke
import miragefairy2024.util.register
import miragefairy2024.util.text
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeKeys

class TraitCard(
    path: String,
    val enName: String,
    val jaName: String,
    enPoem: String,
    jaPoem: String,
    private val traitConditionCards: List<TraitConditionCard>,
    private val traitEffectKeyCardStacks: List<Pair<TraitEffectKeyCard, Double>>,
    private val spawnSpecConfigurator: MutableList<TraitSpawnSpec>.() -> Unit,
) {
    companion object {
        val entries = mutableListOf<TraitCard>()
        operator fun TraitCard.not() = also { entries += this }

        val ETHER_RESPIRATION = !TraitCard(
            "ether_respiration", "Ether Respiration", "エーテル呼吸",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.NUTRITION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.ALWAYS)
            register("0100", TraitSpawnRarity.RARE) { end }
            register("0010", TraitSpawnRarity.RARE)
            register("0001", TraitSpawnRarity.S_RARE)
        }
        val PHOTOSYNTHESIS = !TraitCard(
            "photosynthesis", "Photosynthesis", "光合成",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.LIGHT), listOf(TraitEffectKeyCard.NUTRITION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +ConventionalBiomeTags.SNOWY }
            register("0100", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.DESERT }
            register("0010", TraitSpawnRarity.COMMON) { overworld }
            register("0001", TraitSpawnRarity.RARE) { overworld }
        }
        val PHAEOSYNTHESIS = !TraitCard(
            "phaeosynthesis", "Phaeosynthesis", "闇合成",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.DARKNESS), listOf(TraitEffectKeyCard.NUTRITION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.NORMAL) { nether }
            register("0100", TraitSpawnRarity.RARE) { +BiomeKeys.SOUL_SAND_VALLEY }
            register("0010", TraitSpawnRarity.S_RARE) { nether }
            register("0001", TraitSpawnRarity.NORMAL) { +BiomeKeys.DEEP_DARK }
        }
        val OSMOTIC_ABSORPTION = !TraitCard(
            "osmotic_absorption", "Nutrient Absorption", "養分吸収",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.FLOOR_MOISTURE), listOf(TraitEffectKeyCard.NUTRITION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.COMMON) { +BiomeKeys.SOUL_SAND_VALLEY }
            register("0100", TraitSpawnRarity.S_RARE) { +ConventionalBiomeTags.PLAINS }
            register("0010", TraitSpawnRarity.COMMON) { overworld }
            register("0001", TraitSpawnRarity.RARE) { overworld }
        }
        val CRYSTAL_ABSORPTION = !TraitCard(
            "crystal_absorption", "Crystal Absorption", "鉱物吸収",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.FLOOR_CRYSTAL_ERG), listOf(TraitEffectKeyCard.NUTRITION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.RARE)
            register("0100", TraitSpawnRarity.S_RARE)
            register("0010", TraitSpawnRarity.NORMAL) { +ConventionalBiomeTags.BEACH }
            register("0001", TraitSpawnRarity.NORMAL) { nether }
        }

        val AIR_ADAPTATION = !TraitCard(
            "air_adaptation", "Spatial Adaptation", "空間適応",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.NUTRITION to 0.03, TraitEffectKeyCard.TEMPERATURE to 0.03, TraitEffectKeyCard.HUMIDITY to 0.03),
        ) {
            register("1000", TraitSpawnRarity.ALWAYS)
            register("0100", TraitSpawnRarity.S_RARE)
            register("0010", TraitSpawnRarity.RARE)
            register("0001", TraitSpawnRarity.S_RARE)
        }
        val COLD_ADAPTATION = !TraitCard(
            "cold_adaptation", "Cold Adaptation", "低温適応",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.LOW_TEMPERATURE), listOf(TraitEffectKeyCard.TEMPERATURE to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +TemperatureCategory.LOW }
            register("0100", TraitSpawnRarity.COMMON) { +TemperatureCategory.LOW }
            register("0010", TraitSpawnRarity.RARE) { +TemperatureCategory.LOW }
            register("0001", TraitSpawnRarity.NORMAL) { +TemperatureCategory.LOW }
        }
        val WARM_ADAPTATION = !TraitCard(
            "warm_adaptation", "Warm Adaptation", "中温適応",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.MEDIUM_TEMPERATURE), listOf(TraitEffectKeyCard.TEMPERATURE to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +TemperatureCategory.MEDIUM }
            register("0100", TraitSpawnRarity.COMMON) { +TemperatureCategory.MEDIUM }
            register("0010", TraitSpawnRarity.RARE) { +TemperatureCategory.MEDIUM }
            register("0001", TraitSpawnRarity.NORMAL) { +TemperatureCategory.MEDIUM }
        }
        val HOT_ADAPTATION = !TraitCard(
            "hot_adaptation", "Hot Adaptation", "高温適応",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.HIGH_TEMPERATURE), listOf(TraitEffectKeyCard.TEMPERATURE to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +TemperatureCategory.HIGH }
            register("0100", TraitSpawnRarity.COMMON) { +TemperatureCategory.HIGH }
            register("0010", TraitSpawnRarity.RARE) { +TemperatureCategory.HIGH }
            register("0001", TraitSpawnRarity.NORMAL) { +TemperatureCategory.HIGH }
        }
        val ARID_ADAPTATION = !TraitCard(
            "arid_adaptation", "Arid Adaptation", "乾燥適応",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.LOW_HUMIDITY), listOf(TraitEffectKeyCard.HUMIDITY to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +HumidityCategory.LOW }
            register("0100", TraitSpawnRarity.COMMON) { +HumidityCategory.LOW }
            register("0010", TraitSpawnRarity.RARE) { +HumidityCategory.LOW }
            register("0001", TraitSpawnRarity.NORMAL) { +HumidityCategory.LOW }
        }
        val MESIC_ADAPTATION = !TraitCard(
            "mesic_adaptation", "Mesic Adaptation", "中湿適応",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.MEDIUM_HUMIDITY), listOf(TraitEffectKeyCard.HUMIDITY to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +HumidityCategory.MEDIUM }
            register("0100", TraitSpawnRarity.COMMON) { +HumidityCategory.MEDIUM }
            register("0010", TraitSpawnRarity.RARE) { +HumidityCategory.MEDIUM }
            register("0001", TraitSpawnRarity.NORMAL) { +HumidityCategory.MEDIUM }
        }
        val HUMID_ADAPTATION = !TraitCard(
            "humid_adaptation", "Humid Adaptation", "湿潤適応",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.HIGH_HUMIDITY), listOf(TraitEffectKeyCard.HUMIDITY to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +HumidityCategory.HIGH }
            register("0100", TraitSpawnRarity.COMMON) { +HumidityCategory.HIGH }
            register("0010", TraitSpawnRarity.RARE) { +HumidityCategory.HIGH }
            register("0001", TraitSpawnRarity.NORMAL) { +HumidityCategory.HIGH }
        }

        val SEEDS_PRODUCTION = !TraitCard(
            "seeds_production", "Seeds Production", "種子生成",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.SEEDS_PRODUCTION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.RARE)
            register("0100", TraitSpawnRarity.S_RARE)
            register("0010", TraitSpawnRarity.COMMON)
            register("0001", TraitSpawnRarity.RARE)
        }
        val FRUITS_PRODUCTION = !TraitCard(
            "fruits_production", "Fruits Production", "果実生成",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.FRUITS_PRODUCTION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.RARE)
            register("0100", TraitSpawnRarity.S_RARE)
            register("0010", TraitSpawnRarity.COMMON)
            register("0001", TraitSpawnRarity.RARE)
        }
        val LEAVES_PRODUCTION = !TraitCard(
            "leaves_production", "Leaves Production", "葉面生成",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.LEAVES_PRODUCTION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.RARE)
            register("0100", TraitSpawnRarity.S_RARE)
            register("0010", TraitSpawnRarity.COMMON)
            register("0001", TraitSpawnRarity.RARE)
        }
        val RARE_PRODUCTION = !TraitCard(
            "rare_production", "Rare Production", "希少品生成",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.RARE_PRODUCTION to 0.1 * 0.03),
        ) {
            register("1000", TraitSpawnRarity.RARE)
            register("0100", TraitSpawnRarity.S_RARE)
            register("0010", TraitSpawnRarity.COMMON)
            register("0001", TraitSpawnRarity.RARE)
        }
        val EXPERIENCE_PRODUCTION = !TraitCard(
            "experience_production", "Xp Production", "経験値生成",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.EXPERIENCE_PRODUCTION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE)
            //register("0100", )
            register("0010", TraitSpawnRarity.RARE)
            register("0001", TraitSpawnRarity.S_RARE)
        }

        val FAIRY_BLESSING = !TraitCard(
            "fairy_blessing", "Fairy Blessing", "妖精の祝福",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.FORTUNE_FACTOR to 0.1),
        ) {
            //register("1000", )
            register("0100", TraitSpawnRarity.S_RARE)
            register("0010", TraitSpawnRarity.COMMON)
            register("0001", TraitSpawnRarity.RARE)
        }

        val FOUR_LEAFED = !TraitCard(
            "four_leafed", "Four-leafed", "四つ葉",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.FORTUNE_FACTOR to 0.1),
        ) {
            register("1000", TraitSpawnRarity.NORMAL) { +BiomeKeys.WARPED_FOREST }
            register("0100", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.FLORAL }
            register("0010", TraitSpawnRarity.S_RARE)
            register("0001", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.PLAINS }
        }
        val NODED_STEM = !TraitCard(
            "noded_stem", "Noded Stem", "節状の茎",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.GROWTH_BOOST to 0.1),
        ) {
            //r("1000", )
            register("0100", TraitSpawnRarity.COMMON) { +BiomeKeys.BAMBOO_JUNGLE }
            register("0010", TraitSpawnRarity.NORMAL) { +ConventionalBiomeTags.BEACH }
            register("0001", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.JUNGLE }
        }
        val FRUIT_OF_KNOWLEDGE = !TraitCard(
            "fruit_of_knowledge", "Forbidden Fruit", "禁断の果実",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.EXPERIENCE_PRODUCTION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +HumidityCategory.HIGH }
            register("0100", TraitSpawnRarity.COMMON) { +ConventionalBiomeTags.JUNGLE }
            register("0010", TraitSpawnRarity.RARE) { +BiomeKeys.WARPED_FOREST }
            register("0001", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.FOREST }
        }
        val GOLDEN_APPLE = !TraitCard(
            "golden_apple", "Golden Apple", "金のリンゴ",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.FORTUNE_FACTOR to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +ConventionalBiomeTags.FOREST }
            register("0100", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.FOREST }
            register("0010", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.JUNGLE }
            register("0001", TraitSpawnRarity.S_RARE) { +ConventionalBiomeTags.FOREST }
        }
        val SPINY_LEAVES = !TraitCard(
            "spiny_leaves", "Spiny Leaves", "棘のある葉",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.LOW_HUMIDITY), listOf(TraitEffectKeyCard.HUMIDITY to 0.1), // TODO 接触ダメージ
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +HumidityCategory.LOW }
            register("0100", TraitSpawnRarity.NORMAL) { +ConventionalBiomeTags.MESA }
            register("0010", TraitSpawnRarity.NORMAL) { nether }
            register("0001", TraitSpawnRarity.NORMAL) { +ConventionalBiomeTags.DESERT }
        }
        val DESERT_GEM = !TraitCard(
            "desert_gem", "Desert Gem", "砂漠の宝石",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.HIGH_TEMPERATURE, TraitConditionCard.LOW_HUMIDITY), listOf(TraitEffectKeyCard.RARE_PRODUCTION to 0.1 * 0.03),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +HumidityCategory.LOW }
            register("0100", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.DESERT }
            register("0010", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.MESA }
            register("0001", TraitSpawnRarity.S_RARE) { +ConventionalBiomeTags.DESERT }
        }
        val HEATING_MECHANISM = !TraitCard(
            "heating_mechanism", "Heating Mechanism", "発熱機構",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.LOW_TEMPERATURE), listOf(TraitEffectKeyCard.TEMPERATURE to 0.05, TraitEffectKeyCard.GROWTH_BOOST to 0.05),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +TemperatureCategory.LOW }
            register("0100", TraitSpawnRarity.COMMON) { +ConventionalBiomeTags.SNOWY }
            register("0010", TraitSpawnRarity.NORMAL) { +ConventionalBiomeTags.ICY }
            register("0001", TraitSpawnRarity.NORMAL) { +ConventionalBiomeTags.TAIGA }
        }
        val WATERLOGGING_TOLERANCE = !TraitCard(
            "waterlogging_tolerance", "Waterlogging Tolerance", "浸水耐性",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.HIGH_HUMIDITY), listOf(TraitEffectKeyCard.HUMIDITY to 0.05, TraitEffectKeyCard.GROWTH_BOOST to 0.05),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +HumidityCategory.HIGH }
            register("0100", TraitSpawnRarity.NORMAL) { +ConventionalBiomeTags.RIVER }
            register("0010", TraitSpawnRarity.NORMAL) { +ConventionalBiomeTags.SWAMP }
            register("0001", TraitSpawnRarity.NORMAL) { +BiomeKeys.LUSH_CAVES }
        }
        val ADVERSITY_FLOWER = !TraitCard(
            "adversity_flower", "Adversity Flower", "高嶺の花",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.HIGH_ALTITUDE), listOf(TraitEffectKeyCard.FRUITS_PRODUCTION to 0.05, TraitEffectKeyCard.RARE_PRODUCTION to 0.05 * 0.03),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +ConventionalBiomeTags.MOUNTAIN }
            register("0100", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.MOUNTAIN }
            register("0010", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.MOUNTAIN }
            register("0001", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.EXTREME_HILLS }
        }
        val FLESHY_LEAVES = !TraitCard(
            "fleshy_leaves", "Fleshy Leaves", "肉厚の葉",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.LOW_HUMIDITY), listOf(TraitEffectKeyCard.HUMIDITY to 0.05, TraitEffectKeyCard.LEAVES_PRODUCTION to 0.05),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +HumidityCategory.LOW }
            register("0100", TraitSpawnRarity.NORMAL) { +ConventionalBiomeTags.SAVANNA }
            register("0010", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.DESERT }
            register("0001", TraitSpawnRarity.RARE) { nether }
        }
        val NATURAL_ABSCISSION = !TraitCard(
            "natural_abscission", "Natural Abscission", "自然落果",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.NATURAL_ABSCISSION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE)
            register("0100", TraitSpawnRarity.NORMAL) { +BiomeKeys.ICE_SPIKES }
            register("0010", TraitSpawnRarity.NORMAL) { +BiomeKeys.BASALT_DELTAS }
            register("0001", TraitSpawnRarity.NORMAL) { +BiomeKeys.DRIPSTONE_CAVES }
        }
        val CARNIVOROUS_PLANT = !TraitCard(
            "carnivorous_plant", "Carnivorous Plant", "食虫植物",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.OUTDOOR), listOf(TraitEffectKeyCard.NUTRITION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { nether }
            register("0100", TraitSpawnRarity.NORMAL) { +ConventionalBiomeTags.SWAMP }
            register("0010", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.MOUNTAIN }
            register("0001", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.JUNGLE }
        }
        val ETHER_PREDATION = !TraitCard(
            "ether_predation", "Ether Predation", "エーテル捕食",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.FRUITS_PRODUCTION to 0.05, TraitEffectKeyCard.NUTRITION to 0.05),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { end }
            register("0100", TraitSpawnRarity.NORMAL) { end }
            register("0010", TraitSpawnRarity.COMMON)
            register("0001", TraitSpawnRarity.RARE)
        }
        val PAVEMENT_FLOWERS = !TraitCard(
            "pavement_flowers", "Pavement Flowers", "アスファルトに咲く花",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.FLOOR_HARDNESS), listOf(TraitEffectKeyCard.NUTRITION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { nether }
            register("0100", TraitSpawnRarity.COMMON) { nether }
            register("0010", TraitSpawnRarity.COMMON) { +ConventionalBiomeTags.CAVES }
            register("0001", TraitSpawnRarity.RARE) { +BiomeKeys.BASALT_DELTAS }
        }
        val PROSPERITY_OF_SPECIES = !TraitCard(
            "prosperity_of_species", "Prosperity of Species", "種の繁栄",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.SEEDS_PRODUCTION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.S_RARE) { +HumidityCategory.MEDIUM }
            register("0100", TraitSpawnRarity.COMMON) { +ConventionalBiomeTags.PLAINS }
            register("0010", TraitSpawnRarity.RARE) { +ConventionalBiomeTags.FOREST }
            register("0001", TraitSpawnRarity.S_RARE) { +TemperatureCategory.MEDIUM }
        }
        val PHANTOM_FLOWER = !TraitCard(
            "phantom_flower", "Phantom Flower", "幻の花",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.FORTUNE_FACTOR to 0.1),
        ) {
            //register("1000", )
            //register("0100", )
            register("0010", TraitSpawnRarity.COMMON) { +FAIRY_BIOME_TAG }
            register("0001", TraitSpawnRarity.RARE) { +FAIRY_BIOME_TAG }
        }
        val ETERNAL_TREASURE = !TraitCard(
            "eternal_treasure", "Eternal Treasure", "悠久の秘宝",
            "TODO", // TODO
            "TODO", // TODO
            listOf(TraitConditionCard.NATURAL), listOf(TraitEffectKeyCard.RARE_PRODUCTION to 0.1),
        ) {
            register("1000", TraitSpawnRarity.COMMON)
            //register("0100", )
            register("0010", TraitSpawnRarity.COMMON)
            //register("0001", )
        }
        val TREASURE_OF_XARPA = !TraitCard(
            "treasure_of_xarpa", "Treasure of Xarpa", "シャルパの秘宝",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.RARE_PRODUCTION to 0.03 * 0.03),
        ) {
            register("1000", TraitSpawnRarity.RARE) { +BiomeKeys.DEEP_DARK }
            //register("0100", )
            register("0010", TraitSpawnRarity.RARE) { +BiomeKeys.LUSH_CAVES }
            register("0001", TraitSpawnRarity.NORMAL) { +BiomeKeys.LUSH_CAVES }
            // TODO シャルパの遺伝子改良
        }
        val CROSSBREEDING = !TraitCard(
            "crossbreeding", "Crossbreeding", "交雑",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.CROSSBREEDING to 0.1),
        ) {
            //register("1000", )
            //register("0100", )
            register("0010", TraitSpawnRarity.RARE)
            register("0001", TraitSpawnRarity.RARE)
        }
        val PLANTS_WITH_SELF_AWARENESS = !TraitCard(
            "plants_with_self_awareness", "Plants with Self-Awareness", "自我を持つ植物",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.FRUITS_PRODUCTION to 0.05, TraitEffectKeyCard.EXPERIENCE_PRODUCTION to 0.05),
        ) {
            //register("1000", TraitSpawnRarity.COMMON)
            register("0100", TraitSpawnRarity.S_RARE) { +FAIRY_BIOME_TAG }
            register("0010", TraitSpawnRarity.RARE) { +BiomeCards.FAIRY_FOREST.registryKey }
            register("0001", TraitSpawnRarity.COMMON)
        }
        val FLOWER_OF_THE_END = !TraitCard(
            "flower_of_the_end", "Flower of the End", "終焉の花",
            "TODO", // TODO
            "TODO", // TODO
            listOf(), listOf(TraitEffectKeyCard.FORTUNE_FACTOR to 0.05, TraitEffectKeyCard.EXPERIENCE_PRODUCTION to 0.05),
        ) {
            register("1000", TraitSpawnRarity.ALWAYS)
            //register("0100", )
            //register("0010", )
            //register("0001", )
        }
    }

    init {
        check(traitEffectKeyCardStacks.isNotEmpty())
    }

    val identifier = MirageFairy2024.identifier(path)
    val poemTranslation = Translation({ identifier.toTranslationKey("${MirageFairy2024.modId}.trait", "poem") }, enPoem, jaPoem)
    val trait: Trait = object : Trait(traitEffectKeyCardStacks.first().first.traitEffectKey.style, text { poemTranslation() }) {
        override val spawnSpecs = mutableListOf<TraitSpawnSpec>().also { spawnSpecConfigurator(it) }

        override val conditions = traitConditionCards.map { it.traitCondition }
        override val primaryEffect = traitEffectKeyCardStacks.first().first.traitEffectKey
        override val effectStacks = traitEffectKeyCardStacks.map { Pair(it.first.traitEffectKey, it.second) }

        override fun getTraitEffects(world: World, blockPos: BlockPos, level: Int): MutableTraitEffects? {
            val factor = traitConditionCards.map { it.traitCondition.getFactor(world, blockPos) }.fold(1.0) { a, b -> a * b }
            return if (factor != 0.0) {
                val traitEffects = MutableTraitEffects()
                traitEffectKeyCardStacks.forEach {
                    traitEffects[it.first.traitEffectKey] = it.first.traitEffectKey.getValue(it.second * level) * factor
                }
                traitEffects
            } else {
                null
            }
        }
    }

}

private fun MutableList<TraitSpawnSpec>.register(binary: String, rarity: TraitSpawnRarity, conditionGetter: context(TraitSpawnConditionScope)() -> TraitSpawnCondition = { anywhere }) {
    this += TraitSpawnSpec(conditionGetter(TraitSpawnConditionScope), rarity, binary.toInt(2))
}


context(ModContext)
fun initTraitCard() {
    TraitCard.entries.forEach { card ->
        card.poemTranslation.enJa()
        card.trait.register(traitRegistry, card.identifier)
        card.trait.enJa(card.enName, card.jaName)
    }
}

package miragefairy2024.mod

import com.mojang.datafixers.util.Pair
import miragefairy2024.MirageFairy2024
import miragefairy2024.ModEvents
import miragefairy2024.TerraBlenderEvents
import miragefairy2024.mod.haimeviska.haimeviskaFairyForestPlacedFeatureKey
import miragefairy2024.mod.magicplant.magicplants.mirageClusterFairyForestPlacedFeatureKey
import miragefairy2024.util.Translation
import miragefairy2024.util.enJa
import miragefairy2024.util.registerBiomeTagGeneration
import miragefairy2024.util.registerDynamicGeneration
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeEffects
import net.minecraft.world.biome.GenerationSettings
import net.minecraft.world.biome.SpawnSettings
import net.minecraft.world.biome.source.util.MultiNoiseUtil
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.carver.ConfiguredCarver
import net.minecraft.world.gen.feature.DefaultBiomeFeatures
import net.minecraft.world.gen.feature.OceanPlacedFeatures
import net.minecraft.world.gen.feature.PlacedFeature
import terrablender.api.ParameterUtils
import terrablender.api.Region
import terrablender.api.RegionType
import terrablender.api.Regions
import java.util.function.Consumer

@Suppress("unused")
object BiomeCards {
    val entries = mutableListOf<BiomeCard>()

    val FAIRY_FOREST = FairyForestBiomeCard.also { entries += it }
}

abstract class BiomeCard(
    path: String,
    en: String,
    ja: String,
    val regionType: RegionType,
    val weight: Int,
    val temperature: MultiNoiseUtil.ParameterRange,
    val humidity: MultiNoiseUtil.ParameterRange,
    val continentalness: MultiNoiseUtil.ParameterRange,
    val erosion: MultiNoiseUtil.ParameterRange,
    val weirdness: MultiNoiseUtil.ParameterRange,
    val depth: MultiNoiseUtil.ParameterRange,
    val offset: Float,
    vararg val tags: TagKey<Biome>,
) {
    abstract fun createBiome(placedFeatureLookup: RegistryEntryLookup<PlacedFeature>, configuredCarverLookup: RegistryEntryLookup<ConfiguredCarver<*>>): Biome
    val identifier = Identifier(MirageFairy2024.modId, path)
    val registryKey: RegistryKey<Biome> = RegistryKey.of(RegistryKeys.BIOME, identifier)
    val biomeTag: TagKey<Biome> = TagKey.of(RegistryKeys.BIOME, identifier)
    val translation = Translation({ identifier.toTranslationKey("biome") }, en, ja)
}

fun initBiomeModule() {
    ModEvents.onInitialize {
        BiomeCards.entries.forEach { card ->

            // バイオームの生成
            registerDynamicGeneration(RegistryKeys.BIOME, card.registryKey) {
                card.createBiome(it.getRegistryLookup(RegistryKeys.PLACED_FEATURE), it.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER))
            }

            // このバイオームを指定するバイオームタグの生成
            card.identifier.registerBiomeTagGeneration { card.biomeTag }

            // このバイオームをタグに登録
            card.tags.forEach { tag ->
                card.identifier.registerBiomeTagGeneration { tag }
            }

            // 翻訳生成
            card.translation.enJa()

        }
    }
    TerraBlenderEvents.onTerraBlenderInitialized {
        BiomeCards.entries.forEach { card ->

            // バイオームをTerraBlenderに登録
            Regions.register(object : Region(card.identifier, card.regionType, card.weight) {
                override fun addBiomes(registry: Registry<Biome>, mapper: Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>>) {
                    addBiome(mapper, card.temperature, card.humidity, card.continentalness, card.erosion, card.weirdness, card.depth, card.offset, card.registryKey)
                }
            })

        }
    }
}


object FairyForestBiomeCard : BiomeCard(
    "fairy_forest", "Fairy Forest", "妖精の森",
    RegionType.OVERWORLD, 1,
    ParameterUtils.Temperature.span(ParameterUtils.Temperature.COOL, ParameterUtils.Temperature.COOL),
    ParameterUtils.Humidity.span(ParameterUtils.Humidity.WET, ParameterUtils.Humidity.WET),
    ParameterUtils.Continentalness.span(ParameterUtils.Continentalness.FAR_INLAND, ParameterUtils.Continentalness.FAR_INLAND),
    ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_0),
    ParameterUtils.Weirdness.span(ParameterUtils.Weirdness.MID_SLICE_VARIANT_DESCENDING, ParameterUtils.Weirdness.MID_SLICE_VARIANT_DESCENDING),
    ParameterUtils.Depth.span(ParameterUtils.Depth.SURFACE, ParameterUtils.Depth.SURFACE),
    0.95F,
    BiomeTags.IS_FOREST, ConventionalBiomeTags.FLORAL,
) {
    override fun createBiome(placedFeatureLookup: RegistryEntryLookup<PlacedFeature>, configuredCarverLookup: RegistryEntryLookup<ConfiguredCarver<*>>): Biome {
        return Biome.Builder()
            .precipitation(true)
            .temperature(0.4F)
            .downfall(0.6F)
            .effects(
                BiomeEffects.Builder()
                    .waterColor(0xF3D9FF)
                    .waterFogColor(0xF3D9FF)
                    .fogColor(0xF7AFFF)
                    .skyColor(0xF7AFFF)
                    .grassColor(0x82FFBF)
                    .foliageColor(0xCDAFFF)
                    .build()
            )
            .spawnSettings(SpawnSettings.Builder().also { spawnSettings ->

                DefaultBiomeFeatures.addCaveMobs(spawnSettings)

                spawnSettings.spawn(SpawnGroup.CREATURE, SpawnSettings.SpawnEntry(EntityType.RABBIT, 4, 2, 3))
                spawnSettings.spawn(SpawnGroup.CREATURE, SpawnSettings.SpawnEntry(EntityType.FOX, 8, 2, 4))

                spawnSettings.spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 10, 1, 4))

                // River Mobs
                spawnSettings.spawn(SpawnGroup.WATER_CREATURE, SpawnSettings.SpawnEntry(EntityType.SQUID, 2, 1, 4))
                spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, SpawnSettings.SpawnEntry(EntityType.SALMON, 5, 1, 5))

            }.build())
            .generationSettings(GenerationSettings.LookupBackedBuilder(placedFeatureLookup, configuredCarverLookup).also { lookupBackedBuilder ->

                // BasicFeatures
                DefaultBiomeFeatures.addLandCarvers(lookupBackedBuilder)
                DefaultBiomeFeatures.addAmethystGeodes(lookupBackedBuilder)
                DefaultBiomeFeatures.addDungeons(lookupBackedBuilder)
                DefaultBiomeFeatures.addMineables(lookupBackedBuilder)
                DefaultBiomeFeatures.addSprings(lookupBackedBuilder)
                DefaultBiomeFeatures.addFrozenTopLayer(lookupBackedBuilder)

                DefaultBiomeFeatures.addLargeFerns(lookupBackedBuilder)

                DefaultBiomeFeatures.addDefaultOres(lookupBackedBuilder)
                DefaultBiomeFeatures.addDefaultDisks(lookupBackedBuilder)

                lookupBackedBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, haimeviskaFairyForestPlacedFeatureKey)
                DefaultBiomeFeatures.addForestTrees(lookupBackedBuilder)

                DefaultBiomeFeatures.addDefaultFlowers(lookupBackedBuilder)
                DefaultBiomeFeatures.addMeadowFlowers(lookupBackedBuilder)
                DefaultBiomeFeatures.addTaigaGrass(lookupBackedBuilder)
                DefaultBiomeFeatures.addDefaultMushrooms(lookupBackedBuilder)
                DefaultBiomeFeatures.addDefaultVegetation(lookupBackedBuilder)
                DefaultBiomeFeatures.addSweetBerryBushes(lookupBackedBuilder)
                lookupBackedBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, mirageClusterFairyForestPlacedFeatureKey)

                lookupBackedBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEAGRASS_RIVER)

            }.build()).build()
    }
}

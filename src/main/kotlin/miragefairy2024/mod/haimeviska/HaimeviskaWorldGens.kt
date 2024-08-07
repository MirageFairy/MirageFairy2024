package miragefairy2024.mod.haimeviska

import com.mojang.serialization.Codec
import miragefairy2024.MirageFairy2024
import miragefairy2024.ModContext
import miragefairy2024.util.register
import miragefairy2024.util.registerDynamicGeneration
import miragefairy2024.util.registerFeature
import miragefairy2024.util.with
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.PillarBlock
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.intprovider.ConstantIntProvider
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.PlacedFeature
import net.minecraft.world.gen.feature.PlacedFeatures
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier
import net.minecraft.world.gen.placementmodifier.SurfaceWaterDepthFilterPlacementModifier
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.treedecorator.TreeDecorator
import net.minecraft.world.gen.treedecorator.TreeDecoratorType
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer
import java.util.OptionalInt

object HaimeviskaTreeDecoratorCard {
    val identifier = Identifier(MirageFairy2024.modId, "haimeviska")
    val treeDecorator = HaimeviskaTreeDecorator()
    private val codec: Codec<HaimeviskaTreeDecorator> = Codec.unit { treeDecorator }
    val type: TreeDecoratorType<HaimeviskaTreeDecorator> = TreeDecoratorType(codec)
}

val haimeviskaConfiguredFeatureKey: RegistryKey<ConfiguredFeature<*, *>> = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier(MirageFairy2024.modId, "haimeviska"))
val haimeviskaPlacedFeatureKey: RegistryKey<PlacedFeature> = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier(MirageFairy2024.modId, "haimeviska"))
val haimeviskaFairyForestPlacedFeatureKey: RegistryKey<PlacedFeature> = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier(MirageFairy2024.modId, "haimeviska_fairy_forest"))
val haimeviskaDeepFairyForestPlacedFeatureKey: RegistryKey<PlacedFeature> = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier(MirageFairy2024.modId, "haimeviska_deep_fairy_forest"))

context(ModContext)
fun initHaimeviskaWorldGens() {

    // TreeDecoratorの登録
    HaimeviskaTreeDecoratorCard.type.register(Registries.TREE_DECORATOR_TYPE, HaimeviskaTreeDecoratorCard.identifier)

    // ConfiguredFeatureの登録
    registerDynamicGeneration(RegistryKeys.CONFIGURED_FEATURE, haimeviskaConfiguredFeatureKey) {
        Feature.TREE with TreeFeatureConfig.Builder(
            BlockStateProvider.of(HaimeviskaBlockCard.LOG.block),
            LargeOakTrunkPlacer(22, 10, 0), // 最大32
            BlockStateProvider.of(HaimeviskaBlockCard.LEAVES.block),
            LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(2), 4),
            TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)),
        ).ignoreVines().decorators(listOf(HaimeviskaTreeDecoratorCard.treeDecorator)).build()
    }

    // まばらなPlacedFeature
    registerDynamicGeneration(RegistryKeys.PLACED_FEATURE, haimeviskaPlacedFeatureKey) {
        val placementModifiers = listOf(
            RarityFilterPlacementModifier.of(512),
            SquarePlacementModifier.of(),
            SurfaceWaterDepthFilterPlacementModifier.of(0),
            PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
            BiomePlacementModifier.of(),
            PlacedFeatures.wouldSurvive(HaimeviskaBlockCard.SAPLING.block),
        )
        it.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE).getOrThrow(haimeviskaConfiguredFeatureKey) with placementModifiers
    }

    // 高密度のPlacedFeature
    registerDynamicGeneration(RegistryKeys.PLACED_FEATURE, haimeviskaFairyForestPlacedFeatureKey) {
        val placementModifiers = listOf(
            RarityFilterPlacementModifier.of(16),
            SquarePlacementModifier.of(),
            SurfaceWaterDepthFilterPlacementModifier.of(0),
            PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
            BiomePlacementModifier.of(),
            PlacedFeatures.wouldSurvive(HaimeviskaBlockCard.SAPLING.block),
        )
        it.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE).getOrThrow(haimeviskaConfiguredFeatureKey) with placementModifiers
    }

    // 超高密度のPlacedFeature
    registerDynamicGeneration(RegistryKeys.PLACED_FEATURE, haimeviskaDeepFairyForestPlacedFeatureKey) {
        val placementModifiers = listOf(
            CountPlacementModifier.of(8),
            SquarePlacementModifier.of(),
            SurfaceWaterDepthFilterPlacementModifier.of(0),
            PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
            BiomePlacementModifier.of(),
            PlacedFeatures.wouldSurvive(HaimeviskaBlockCard.SAPLING.block),
        )
        it.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE).getOrThrow(haimeviskaConfiguredFeatureKey) with placementModifiers
    }

    // 平原・森林バイオームに配置
    registerFeature(BiomeSelectors.tag(ConventionalBiomeTags.PLAINS).or(BiomeSelectors.tag(ConventionalBiomeTags.FOREST)), GenerationStep.Feature.VEGETAL_DECORATION, haimeviskaPlacedFeatureKey)

}

class HaimeviskaTreeDecorator : TreeDecorator() {
    override fun getType() = HaimeviskaTreeDecoratorCard.type
    override fun generate(generator: Generator) {
        generator.logPositions.forEach { blockPos ->
            if (!generator.world.testBlockState(blockPos) { it == HaimeviskaBlockCard.LOG.block.defaultState.with(PillarBlock.AXIS, Direction.Axis.Y) }) return@forEach // 垂直の幹のみ
            val direction = Direction.fromHorizontal(generator.random.nextInt(4))
            if (!generator.isAir(blockPos.offset(direction))) return@forEach // 正面が空気の場合のみ
            val r = generator.random.nextInt(100)
            if (r < 25) {
                generator.replace(blockPos, HaimeviskaBlockCard.DRIPPING_LOG.block.defaultState.with(HorizontalFacingBlock.FACING, direction))
            } else if (r < 35) {
                generator.replace(blockPos, HaimeviskaBlockCard.HOLLOW_LOG.block.defaultState.with(HorizontalFacingBlock.FACING, direction))
            }
        }
    }
}

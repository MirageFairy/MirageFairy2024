package miragefairy2024

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.item.Item
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

object MirageFairy2024DataGenerator : DataGeneratorEntrypoint {

    val blockStateModelGenerators = DataGeneratorRegistry<BlockStateModelGenerator>()
    val itemModelGenerators = DataGeneratorRegistry<ItemModelGenerator>()
    val blockTagGenerators = DataGeneratorRegistry<(TagKey<Block>) -> FabricTagProvider<Block>.FabricTagBuilder>()
    val itemTagGenerators = DataGeneratorRegistry<(TagKey<Item>) -> FabricTagProvider<Item>.FabricTagBuilder>()
    val blockLootTableGenerators = DataGeneratorRegistry<FabricBlockLootTableProvider>()
    val recipeGenerators = DataGeneratorRegistry<RecipeExporter>()
    val englishTranslationGenerators = DataGeneratorRegistry<FabricLanguageProvider.TranslationBuilder>()
    val japaneseTranslationGenerators = DataGeneratorRegistry<FabricLanguageProvider.TranslationBuilder>()

    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack = fabricDataGenerator.createPack()
        pack.addProvider { output: FabricDataOutput ->
            object : FabricModelProvider(output) {
                override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) = blockStateModelGenerators.fire { it(blockStateModelGenerator) }
                override fun generateItemModels(itemModelGenerator: ItemModelGenerator) = itemModelGenerators.fire { it(itemModelGenerator) }
            }
        }
        pack.addProvider { output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup> ->
            object : FabricTagProvider.BlockTagProvider(output, registriesFuture) {
                override fun configure(arg: RegistryWrapper.WrapperLookup) = blockTagGenerators.fire { it { tag -> getOrCreateTagBuilder(tag) } }
            }
        }
        pack.addProvider { output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup> ->
            object : FabricTagProvider.ItemTagProvider(output, registriesFuture) {
                override fun configure(arg: RegistryWrapper.WrapperLookup) = itemTagGenerators.fire { it { tag -> getOrCreateTagBuilder(tag) } }
            }
        }
        pack.addProvider { output: FabricDataOutput ->
            object : FabricBlockLootTableProvider(output) {
                override fun generate() = blockLootTableGenerators.fire { it(this) }
            }
        }
        pack.addProvider { output: FabricDataOutput ->
            object : FabricRecipeProvider(output) {
                override fun generate(exporter: RecipeExporter) = recipeGenerators.fire { it(exporter) }
            }
        }
        pack.addProvider { output: FabricDataOutput ->
            object : FabricLanguageProvider(output, "en_us") {
                override fun generateTranslations(translationBuilder: TranslationBuilder) = englishTranslationGenerators.fire { it(translationBuilder) }
            }
        }
        pack.addProvider { output: FabricDataOutput ->
            object : FabricLanguageProvider(output, "ja_jp") {
                override fun generateTranslations(translationBuilder: TranslationBuilder) = japaneseTranslationGenerators.fire { it(translationBuilder) }
            }
        }
    }
}

class DataGeneratorRegistry<T> {
    val list = mutableListOf<(T) -> Unit>()

    operator fun invoke(listener: (T) -> Unit) {
        this.list += listener
    }

    fun fire(processor: ((T) -> Unit) -> Unit) {
        this.list.forEach {
            processor(it)
        }
    }
}

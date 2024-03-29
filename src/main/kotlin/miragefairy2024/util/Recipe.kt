package miragefairy2024.util

import miragefairy2024.MirageFairy2024DataGenerator
import miragefairy2024.mod.recipeGroupRegistry
import net.fabricmc.fabric.api.loot.v2.LootTableEvents
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.block.Blocks
import net.minecraft.block.ComposterBlock
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.loot.condition.LocationCheckLootCondition
import net.minecraft.loot.condition.MatchToolLootCondition
import net.minecraft.loot.condition.RandomChanceLootCondition
import net.minecraft.loot.entry.LeafEntry
import net.minecraft.loot.function.ApplyBonusLootFunction
import net.minecraft.loot.function.ExplosionDecayLootFunction
import net.minecraft.predicate.entity.LocationPredicate
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome

// Crafting

fun <T : CraftingRecipeJsonBuilder> T.criterion(item: Item) = this.also { it.criterion("has_${item.getIdentifier().path}", RecipeProvider.conditionsFromItem(item)) }
fun <T : CraftingRecipeJsonBuilder> T.criterion(tagKey: TagKey<Item>) = this.also { it.criterion("has_${tagKey.id.path}", RecipeProvider.conditionsFromTag(tagKey)) }
fun <T : CraftingRecipeJsonBuilder> T.group(item: Item) = this.also { it.group(recipeGroupRegistry[item] ?: "${item.getIdentifier()}") }

class RecipeGenerationSettings<T> {
    val listeners = mutableListOf<(T) -> Unit>()
    val idModifiers = mutableListOf<(Identifier) -> Identifier>()
    var recipeCategory = RecipeCategory.MISC
}

infix fun <T : CraftingRecipeJsonBuilder> RecipeGenerationSettings<T>.on(item: Item): RecipeGenerationSettings<T> {
    listeners += { it.criterion(item) }
    return this
}

infix fun <T> RecipeGenerationSettings<T>.modId(modId: String): RecipeGenerationSettings<T> {
    idModifiers += { Identifier(modId, it.path) }
    return this
}

infix fun <T> RecipeGenerationSettings<T>.from(item: Item): RecipeGenerationSettings<T> {
    idModifiers += { it concat "_from_" concat item.getIdentifier().path }
    return this
}

fun <T : CraftingRecipeJsonBuilder> registerRecipeGeneration(
    creator: (RecipeCategory, Item, Int) -> T,
    item: Item,
    count: Int = 1,
    block: T.() -> Unit = {},
): RecipeGenerationSettings<T> {
    val settings = RecipeGenerationSettings<T>()
    MirageFairy2024DataGenerator.recipeGenerators {
        val builder = creator(settings.recipeCategory, item, count)
        builder.group(item)
        settings.listeners.forEach { listener ->
            listener(builder)
        }
        block(builder)
        val identifier = settings.idModifiers.fold(item.getIdentifier()) { id, idModifier -> idModifier(id) }
        builder.offerTo(it, identifier)
    }
    return settings
}

fun registerShapedRecipeGeneration(
    item: Item,
    count: Int = 1,
    block: ShapedRecipeJsonBuilder.() -> Unit = {},
): RecipeGenerationSettings<ShapedRecipeJsonBuilder> = registerRecipeGeneration(ShapedRecipeJsonBuilder::create, item, count, block)

fun registerShapelessRecipeGeneration(
    item: Item,
    count: Int = 1,
    block: ShapelessRecipeJsonBuilder.() -> Unit = {},
): RecipeGenerationSettings<ShapelessRecipeJsonBuilder> = registerRecipeGeneration(ShapelessRecipeJsonBuilder::create, item, count, block)

fun registerSmeltingRecipeGeneration(
    input: Item,
    output: Item,
    experience: Double = 0.0,
    cookingTime: Int = 200,
    block: CookingRecipeJsonBuilder.() -> Unit = {},
): RecipeGenerationSettings<CookingRecipeJsonBuilder> {
    val settings = RecipeGenerationSettings<CookingRecipeJsonBuilder>()
    MirageFairy2024DataGenerator.recipeGenerators {
        val builder = CookingRecipeJsonBuilder.create(Ingredient.ofItems(input), RecipeCategory.MISC, output, experience.toFloat(), cookingTime, RecipeSerializer.SMELTING)
        builder.group(output)
        settings.listeners.forEach { listener ->
            listener(builder)
        }
        block(builder)
        val identifier = settings.idModifiers.fold(output.getIdentifier()) { id, idModifier -> idModifier(id) }
        builder.offerTo(it, identifier)
    }
    return settings
}


// Others

fun Item.registerGrassDrop(amount: Float = 1.0F, biome: (() -> RegistryKey<Biome>)? = null) {
    LootTableEvents.MODIFY.register { _, _, id, tableBuilder, source ->
        if (source.isBuiltin) {
            if (id == Blocks.GRASS.lootTableId) {
                tableBuilder.configure {
                    pool(LootPool(AlternativeLootPoolEntry {
                        alternatively(EmptyLootPoolEntry {
                            conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create().items(Items.SHEARS)))
                        })
                        alternatively(ItemLootPoolEntry(this@registerGrassDrop) {
                            conditionally(RandomChanceLootCondition.builder(0.125F * amount))
                            if (biome != null) conditionally(LocationCheckLootCondition.builder(LocationPredicate.Builder.create().biome(biome())))
                            apply(ApplyBonusLootFunction.uniformBonusCount(Enchantments.FORTUNE, 2))
                            apply(ExplosionDecayLootFunction.builder())
                        })
                    }))
                }
            }
        }
    }
}

fun Item.registerChestLoot(lootTableId: Identifier, weight: Int = 10, block: LeafEntry.Builder<*>.() -> Unit = {}) {
    LootTableEvents.MODIFY.register { _, _, id, tableBuilder, source ->
        if (source.isBuiltin) {
            if (id == lootTableId) {
                tableBuilder.modifyPools { lootPool ->
                    lootPool.configure {
                        with(ItemLootPoolEntry(this@registerChestLoot) {
                            weight(weight)
                            block(this)
                        })
                    }
                }
            }
        }
    }
}

fun Item.registerComposterInput(chance: Float) {
    ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(this, chance)
}

/** @param ticks coal is `200 * 8 = 1600` */
fun Item.registerFuel(ticks: Int) {
    FuelRegistry.INSTANCE.add(this, ticks)
}

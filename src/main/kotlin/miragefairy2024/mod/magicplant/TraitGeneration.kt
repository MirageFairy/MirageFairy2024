package miragefairy2024.mod.magicplant

import miragefairy2024.util.HumidityCategory
import miragefairy2024.util.TemperatureCategory
import miragefairy2024.util.humidityCategory
import miragefairy2024.util.temperatureCategory
import miragefairy2024.util.text
import mirrg.kotlin.hydrogen.or
import net.minecraft.block.Block
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome

val worldGenTraitGenerations = mutableListOf<WorldGenTraitGeneration>()

fun interface WorldGenTraitGeneration {
    fun spawn(world: World, blockPos: BlockPos, block: Block): List<TraitStack>
}


class WorldGenTraitRecipe(
    val block: Block,
    val rarity: Rarity,
    val trait: Trait,
    val level: Int,
    val condition: Condition
) {

    enum class Rarity {
        /** 必ず付与される。 */
        A,

        /** 10%の確率で選ばれるN欠損テーブルに乗る。 */
        N,

        /** 10%の確率で選ばれるR獲得テーブルに乗る。 */
        R,

        /** 1%の確率で選ばれるSR獲得テーブルに乗る。 */
        S,
    }

    interface Condition {
        val description: Text
        fun canSpawn(world: World, blockPos: BlockPos): Boolean

        object Always : Condition {
            override val description = text { "always"() } // TODO
            override fun canSpawn(world: World, blockPos: BlockPos) = true
        }

        class InBiome(val biomeTag: TagKey<Biome>) : Condition {
            override val description = text { "in ${biomeTag.id.path}"() } // TODO
            override fun canSpawn(world: World, blockPos: BlockPos) = world.getBiome(blockPos).isIn(biomeTag)
        }

        class NotInBiome(val biomeTag: TagKey<Biome>) : Condition {
            override val description = text { "not in ${biomeTag.id.path}"() } // TODO
            override fun canSpawn(world: World, blockPos: BlockPos) = !world.getBiome(blockPos).isIn(biomeTag)
        }

        class Temperature(val temperature: TemperatureCategory) : Condition {
            override val description = text { "${temperature.name} temperature"() } // TODO
            override fun canSpawn(world: World, blockPos: BlockPos) = world.getBiome(blockPos).temperatureCategory == temperature
        }

        class Humidity(val humidity: HumidityCategory) : Condition {
            override val description = text { "${humidity.name} humidity"() } // TODO
            override fun canSpawn(world: World, blockPos: BlockPos) = world.getBiome(blockPos).humidityCategory == humidity
        }
    }

}

val worldGenTraitRecipeRegistry = mutableMapOf<Block, MutableList<WorldGenTraitRecipe>>()

fun registerWorldGenTraitRecipe(recipe: WorldGenTraitRecipe) {
    worldGenTraitRecipeRegistry.getOrPut(recipe.block) { mutableListOf() } += recipe
}

class RecipeWorldGenTraitGeneration : WorldGenTraitGeneration {
    override fun spawn(world: World, blockPos: BlockPos, block: Block): List<TraitStack> {
        val resultTraitStackList = mutableListOf<TraitStack>()

        // レシピ判定
        val aTraitStackList = mutableListOf<TraitStack>()
        val nTraitStackList = mutableListOf<TraitStack>()
        val rTraitStackList = mutableListOf<TraitStack>()
        val sTraitStackList = mutableListOf<TraitStack>()
        worldGenTraitRecipeRegistry[block].or { listOf() }.forEach { recipe ->
            if (recipe.condition.canSpawn(world, blockPos)) {
                val traitStackList = when (recipe.rarity) {
                    WorldGenTraitRecipe.Rarity.A -> aTraitStackList
                    WorldGenTraitRecipe.Rarity.N -> nTraitStackList
                    WorldGenTraitRecipe.Rarity.R -> rTraitStackList
                    WorldGenTraitRecipe.Rarity.S -> sTraitStackList
                }
                traitStackList += TraitStack(recipe.trait, recipe.level)
            }
        }

        // 抽選
        val r = world.random.nextDouble()
        when {
            r < 0.01 -> { // +S
                resultTraitStackList += aTraitStackList
                resultTraitStackList += nTraitStackList
                if (sTraitStackList.isNotEmpty()) {
                    resultTraitStackList += sTraitStackList[world.random.nextInt(sTraitStackList.size)]
                }
            }

            r >= 0.02 && r < 0.1 -> { // +R
                resultTraitStackList += aTraitStackList
                resultTraitStackList += nTraitStackList
                if (rTraitStackList.isNotEmpty()) {
                    resultTraitStackList += rTraitStackList[world.random.nextInt(rTraitStackList.size)]
                }
            }

            r >= 0.01 && r < 0.02 -> { // -N
                resultTraitStackList += aTraitStackList
                if (nTraitStackList.isNotEmpty()) {
                    nTraitStackList.removeAt(world.random.nextInt(nTraitStackList.size))
                    resultTraitStackList += nTraitStackList
                }
            }

            else -> { // 0
                resultTraitStackList += aTraitStackList
                resultTraitStackList += nTraitStackList
            }
        }

        return resultTraitStackList
    }
}

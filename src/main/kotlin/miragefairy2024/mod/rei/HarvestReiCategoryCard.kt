package miragefairy2024.mod.rei

import me.shedaniel.rei.api.common.display.basic.BasicDisplay
import miragefairy2024.mod.magicplant.HarvestNotation
import miragefairy2024.util.compound
import miragefairy2024.util.get
import miragefairy2024.util.list
import miragefairy2024.util.toEntryIngredient
import miragefairy2024.util.toEntryStack
import miragefairy2024.util.toItemStack
import miragefairy2024.util.toNbt
import miragefairy2024.util.wrapper
import mirrg.kotlin.hydrogen.Single
import mirrg.kotlin.hydrogen.castOrThrow
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList

object HarvestReiCategoryCard : ReiCategoryCard<HarvestReiCategoryCard.Display>("harvest", "Harvest", "収穫") {
    override val serializer: Single<BasicDisplay.Serializer<Display>> by lazy {
        Single(BasicDisplay.Serializer.ofRecipeLess({ _, _, tag ->
            Display(
                HarvestNotation(
                    tag.wrapper["Seed"].compound.get()!!.toItemStack(),
                    tag.wrapper["Crops"].list.get()!!.map { it.castOrThrow<NbtCompound>().toItemStack() },
                )
            )
        }, { display, tag ->
            tag.wrapper["Seed"].set(display.recipe.seed.toNbt())
            tag.wrapper["Crops"].set(display.recipe.crops.mapTo(NbtList()) { it.toNbt() })
        }))
    }

    class Display(val recipe: HarvestNotation) : BasicDisplay(
        listOf(recipe.seed.toEntryStack().toEntryIngredient()),
        recipe.crops.map { it.toEntryStack().toEntryIngredient() },
    ) {
        override fun getCategoryIdentifier() = identifier.first
    }
}

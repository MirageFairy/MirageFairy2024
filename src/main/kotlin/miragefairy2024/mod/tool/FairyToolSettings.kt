package miragefairy2024.mod.tool

import miragefairy2024.MirageFairy2024
import miragefairy2024.ModContext
import miragefairy2024.mod.PoemList
import miragefairy2024.mod.PoemType
import miragefairy2024.mod.translation
import miragefairy2024.util.Translation
import miragefairy2024.util.registerItemTagGeneration
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey

// Api

class FairyToolSettings<I : Item>(
    val creator: (FairyToolSettings<I>) -> I,
    val toolMaterialCard: ToolMaterialCard,
) : ToolSettings<I> {
    companion object {
        val AREA_MINING_TRANSLATION = Translation({ "item.${MirageFairy2024.modId}.fairy_mining_tool.area_mining" }, "Area mining", "範囲採掘")
        val MINE_ALL_TRANSLATION = Translation({ "item.${MirageFairy2024.modId}.fairy_mining_tool.mine_all" }, "Mine the entire ore", "鉱石全体を採掘")
        val CUT_ALL_TRANSLATION = Translation({ "item.${MirageFairy2024.modId}.fairy_mining_tool.cut_all" }, "Cut down the entire tree", "木全体を伐採")
        val SILK_TOUCH_TRANSLATION = Translation({ "item.${MirageFairy2024.modId}.fairy_mining_tool.silk_touch" }, "Silk Touch", "シルクタッチ")
        val SELF_MENDING_TRANSLATION = Translation({ "item.${MirageFairy2024.modId}.fairy_mining_tool.self_mending" }, "Self-mending while in the main hand", "メインハンドにある間、自己修繕")
    }

    val tags = mutableListOf<TagKey<Item>>()
    var attackDamage = 0F
    var attackSpeed = 0F
    val superEffectiveBlocks = mutableListOf<Block>()
    val effectiveBlocks = mutableListOf<Block>()
    val effectiveBlockTags = mutableListOf<TagKey<Block>>()
    var areaMining = false
    var mineAll = false
    var cutAll = false
    var silkTouch = false
    var selfMending: Int? = null
    val descriptions = mutableListOf<Translation>()

    override fun createItem() = creator(this)

    context(ModContext)
    override fun init(card: ToolCard<I>) {
        tags.forEach {
            card.item.registerItemTagGeneration { it }
        }
        card.item.registerItemTagGeneration { toolMaterialCard.tag }
    }

    override fun addPoems(poemList: PoemList) = descriptions.fold(poemList) { it, description -> it.translation(PoemType.DESCRIPTION, description) }

}


// Creator

fun createSword(toolMaterialCard: ToolMaterialCard) = FairyToolSettings({ FairySwordItem(it, Item.Settings()) }, toolMaterialCard).also {
    it.attackDamage = 3.0F
    it.attackSpeed = -2.4F
    it.tags += ItemTags.SWORDS
    it.superEffectiveBlocks += Blocks.COBWEB
    it.effectiveBlockTags += BlockTags.SWORD_EFFICIENT
}

fun createShovel(toolMaterialCard: ToolMaterialCard) = FairyToolSettings({ FairyMiningToolItem(it, Item.Settings()) }, toolMaterialCard).also {
    it.attackDamage = 1.5F
    it.attackSpeed = -3.0F
    it.tags += ItemTags.SHOVELS
    it.effectiveBlockTags += BlockTags.SHOVEL_MINEABLE
}

fun createPickaxe(toolMaterialCard: ToolMaterialCard) = FairyToolSettings({ FairyMiningToolItem(it, Item.Settings()) }, toolMaterialCard).also {
    it.attackDamage = 1F
    it.attackSpeed = -2.8F
    it.tags += ItemTags.PICKAXES
    it.tags += ItemTags.CLUSTER_MAX_HARVESTABLES
    it.effectiveBlockTags += BlockTags.PICKAXE_MINEABLE
}

/**
 * @param attackDamage wood: 6.0, stone: 7.0, gold: 6.0, iron: 6.0, diamond: 5.0, netherite: 5.0
 * @param attackSpeed wood: -3.2, stone: -3.2, gold: -3.0, iron: -3.1, diamond: -3.0, netherite: -3.0
 */
fun createAxe(toolMaterialCard: ToolMaterialCard, attackDamage: Float, attackSpeed: Float) = FairyToolSettings({ FairyMiningToolItem(it, Item.Settings()) }, toolMaterialCard).also {
    it.attackDamage = attackDamage
    it.attackSpeed = attackSpeed
    it.tags += ItemTags.AXES
    it.effectiveBlockTags += BlockTags.AXE_MINEABLE
}

// Hoe
// @param attackDamage wood: 0.0, stone: -1.0, gold: 0.0, iron: -2.0, diamond: -3.0, netherite: -4.0
// @param attackSpeed wood: -3.0, stone: -2.0, gold: -3.0, iron: -1.0, diamond: 0.0, netherite: 0.0


// Configurator

fun FairyToolSettings<*>.areaMining() = this.also {
    it.areaMining = true
    it.descriptions += FairyToolSettings.AREA_MINING_TRANSLATION
}

fun FairyToolSettings<*>.mineAll() = this.also {
    it.mineAll = true
    it.descriptions += FairyToolSettings.MINE_ALL_TRANSLATION
}

fun FairyToolSettings<*>.cutAll() = this.also {
    it.cutAll = true
    it.descriptions += FairyToolSettings.CUT_ALL_TRANSLATION
}

fun FairyToolSettings<*>.silkTouch() = this.also {
    it.silkTouch = true
    it.descriptions += FairyToolSettings.SILK_TOUCH_TRANSLATION
}

fun FairyToolSettings<*>.selfMending(selfMending: Int) = this.also {
    it.selfMending = selfMending
    it.descriptions += FairyToolSettings.SELF_MENDING_TRANSLATION
}
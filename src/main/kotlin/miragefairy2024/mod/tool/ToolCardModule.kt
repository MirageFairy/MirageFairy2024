package miragefairy2024.mod.tool

import miragefairy2024.MirageFairy2024
import miragefairy2024.ModContext
import miragefairy2024.mod.MaterialCard
import miragefairy2024.mod.PoemList
import miragefairy2024.mod.mirageFairy2024ItemGroupCard
import miragefairy2024.mod.poem
import miragefairy2024.mod.registerPoem
import miragefairy2024.mod.registerPoemGeneration
import miragefairy2024.mod.tool.contents.FairyAxeSettings
import miragefairy2024.mod.tool.contents.FairyBattleAxeSettings
import miragefairy2024.mod.tool.contents.FairyKnifeSettings
import miragefairy2024.mod.tool.contents.FairyPickaxeSettings
import miragefairy2024.mod.tool.contents.FairyScytheSettings
import miragefairy2024.mod.tool.contents.FairyShootingStaffSettings
import miragefairy2024.mod.tool.contents.FairyShovelSettings
import miragefairy2024.mod.tool.contents.FairySwordSettings
import miragefairy2024.mod.tool.contents.ScytheItem
import miragefairy2024.mod.tool.contents.ShootingStaffItem
import miragefairy2024.util.EnJa
import miragefairy2024.util.enJa
import miragefairy2024.util.on
import miragefairy2024.util.register
import miragefairy2024.util.registerItemGroup
import miragefairy2024.util.registerModelGeneration
import miragefairy2024.util.registerShapedRecipeGeneration
import net.minecraft.data.client.Models
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.BlockTags

context(ModContext)
fun initToolCardModule() {

    ToolSettings.AREA_MINING_TRANSLATION.enJa()
    ToolSettings.MINE_ALL_TRANSLATION.enJa()
    ToolSettings.CUT_ALL_TRANSLATION.enJa()
    ToolSettings.SILK_TOUCH_TRANSLATION.enJa()
    ToolSettings.FORTUNE_TRANSLATION.enJa()
    ToolSettings.SELF_MENDING_TRANSLATION.enJa()
    ToolSettings.OBTAIN_FAIRY.enJa()

    ScytheItem.DESCRIPTION_TRANSLATION.enJa()
    ShootingStaffItem.NOT_ENOUGH_EXPERIENCE_TRANSLATION.enJa()
    ShootingStaffItem.DESCRIPTION_TRANSLATION.enJa()

    ToolCards.entries.forEach {
        it.init()
    }

}

class ToolCard(
    path: String,
    private val tier: Int,
    private val enName: String,
    private val jaName: String,
    private val enPoem: String,
    private val jaPoem: String,
    private val toolSettings: ToolSettings,
    private val initializer: context(ModContext)ToolCard.() -> Unit = {},
) {
    val identifier = MirageFairy2024.identifier(path)
    val item = toolSettings.createItem()

    context(ModContext)
    fun init() {
        item.register(Registries.ITEM, identifier)

        item.registerItemGroup(mirageFairy2024ItemGroupCard.itemGroupKey)

        item.registerModelGeneration(Models.HANDHELD)

        item.enJa(EnJa(enName, jaName))

        val poemList = PoemList(tier).poem(enPoem, jaPoem).let { toolSettings.appendPoems(it) }
        item.registerPoem(poemList)
        item.registerPoemGeneration(poemList)

        toolSettings.init(this)
        initializer(this@ModContext, this)
    }
}

@Suppress("unused", "MemberVisibilityCanBePrivate")
object ToolCards {
    val entries = mutableListOf<ToolCard>()
    private operator fun ToolCard.not() = this.also { entries.add(this) }

    val IRON_SCYTHE = !ToolCard(
        "iron_scythe", 2, "Iron Scythe", "鉄の大鎌",
        "For cutting grass and harvesting crops.", "草や農作物を刈り取るための道具。",
        FairyScytheSettings(ToolMaterialCard.IRON, 1),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern(" ##")
            pattern("# R")
            pattern("  R")
            input('#', Items.IRON_INGOT)
            input('R', MaterialCard.MIRAGE_STEM.item)
        } on Items.IRON_INGOT
    }
    val FAIRY_CRYSTAL_PICKAXE = !ToolCard(
        "fairy_crystal_pickaxe", 2, "Fairy Crystal Pickaxe", "フェアリークリスタルのつるはし",
        "A brain frozen in crystal", "闇を打ち砕く、透き通る心。",
        FairyPickaxeSettings(ToolMaterialCard.FAIRY_CRYSTAL).selfMending(10).obtainFairy(9.0),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("###")
            pattern(" R ")
            pattern(" R ")
            input('#', MaterialCard.FAIRY_CRYSTAL.item)
            input('R', Items.STICK)
        } on MaterialCard.FAIRY_CRYSTAL.item
    }
    val FAIRY_CRYSTAL_SCYTHE = !ToolCard(
        "fairy_crystal_scythe", 2, "Fairy Crystal Scythe", "フェアリークリスタルの大鎌",
        "What color is fairy blood?", "妖精を刈り取るための道具。",
        FairyScytheSettings(ToolMaterialCard.FAIRY_CRYSTAL, 2).selfMending(10).obtainFairy(9.0),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern(" ##")
            pattern("# R")
            pattern("  R")
            input('#', MaterialCard.FAIRY_CRYSTAL.item)
            input('R', MaterialCard.MIRAGE_STEM.item)
        } on MaterialCard.FAIRY_CRYSTAL.item
    }
    val FAIRY_CRYSTAL_SWORD = !ToolCard(
        "fairy_crystal_sword", 2, "Fairy Crystal Sword", "フェアリークリスタルの剣",
        "Nutrients for the soul", "妖精はこれをおやつにするという",
        FairySwordSettings(ToolMaterialCard.FAIRY_CRYSTAL).selfMending(10).obtainFairy(9.0),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("#")
            pattern("#")
            pattern("R")
            input('#', MaterialCard.FAIRY_CRYSTAL.item)
            input('R', Items.STICK)
        } on MaterialCard.FAIRY_CRYSTAL.item
    }
    val FAIRY_CRYSTAL_BATTLE_AXE = !ToolCard(
        "fairy_crystal_battle_axe", 2, "Fairy Crystal Battle Axe", "フェアリークリスタルの戦斧",
        "The embodiment of fighting spirit", "妖精の本能を呼び覚ませ。",
        FairyBattleAxeSettings(ToolMaterialCard.FAIRY_CRYSTAL, 6.5F, -3.0F).selfMending(10).obtainFairy(9.0),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("###")
            pattern("#R#")
            pattern(" R ")
            input('#', MaterialCard.FAIRY_CRYSTAL.item)
            input('R', Items.STICK)
        } on MaterialCard.FAIRY_CRYSTAL.item
    }
    val MIRAGIUM_PICKAXE = !ToolCard(
        "miragium_pickaxe", 3, "Miragium Pickaxe", "ミラジウムのつるはし",
        "More durable than gold", "妖精の肉体労働",
        FairyPickaxeSettings(ToolMaterialCard.MIRAGIUM).selfMending(20).mineAll(),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("###")
            pattern(" R ")
            pattern(" R ")
            input('#', MaterialCard.MIRAGIUM_INGOT.item)
            input('R', Items.STICK)
        } on MaterialCard.MIRAGIUM_INGOT.item
    }
    val MIRAGIUM_AXE = !ToolCard(
        "miragium_axe", 3, "Miragium Axe", "ミラジウムの斧",
        "Crack! Squish!", "バキッ！ぐにっ",
        FairyAxeSettings(ToolMaterialCard.MIRAGIUM, 5.0F, -3.0F).selfMending(20).cutAll(),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("##")
            pattern("#R")
            pattern(" R")
            input('#', MaterialCard.MIRAGIUM_INGOT.item)
            input('R', Items.STICK)
        } on MaterialCard.MIRAGIUM_INGOT.item
    }
    val MIRANAGITE_KNIFE = !ToolCard(
        "miranagite_knife", 2, "Miranagite Knife", "蒼天石のナイフ",
        "Gardener's tool invented by Miranagi", "大自然を駆ける探究者のナイフ。",
        FairyKnifeSettings(ToolMaterialCard.MIRANAGITE).silkTouch(),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("#")
            pattern("R")
            input('#', MaterialCard.MIRANAGITE.item)
            input('R', Items.STICK)
        } on MaterialCard.MIRANAGITE.item
    }
    val MIRANAGITE_PICKAXE = !ToolCard(
        "miranagite_pickaxe", 2, "Miranagite Pickaxe", "蒼天石のつるはし",
        "Promotes ore recrystallization", "凝集する秩序、蒼穹彩煌が如く。",
        FairyPickaxeSettings(ToolMaterialCard.MIRANAGITE).silkTouch(),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("###")
            pattern(" R ")
            pattern(" R ")
            input('#', MaterialCard.MIRANAGITE.item)
            input('R', Items.STICK)
        } on MaterialCard.MIRANAGITE.item
    }
    val MIRANAGITE_SCYTHE = !ToolCard(
        "miranagite_scythe", 2, "Miranagite Scythe", "蒼天石の大鎌",
        "Releases the souls of weeds", "宙を切り裂く創世の刃、草魂を蒼天へ導く。",
        FairyScytheSettings(ToolMaterialCard.MIRANAGITE, 3).silkTouch(),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern(" ##")
            pattern("# R")
            pattern("  R")
            input('#', MaterialCard.MIRANAGITE.item)
            input('R', MaterialCard.MIRAGE_STEM.item)
        } on MaterialCard.MIRANAGITE.item
    }
    val MIRANAGI_STAFF_0 = !ToolCard(
        "miranagi_staff_0", 2, "Miranagite Staff", "蒼天石のスタッフ",
        "Inflating anti-entropy force", "膨張する秩序の力。",
        FairyShootingStaffSettings(ToolMaterialCard.MIRANAGITE, 7F, 12F).silkTouch(),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern(" IG")
            pattern(" RI")
            pattern("I  ")
            input('R', MaterialCard.MIRANAGITE_ROD.item)
            input('G', Items.GLASS)
            input('I', Items.COPPER_INGOT)
        } on MaterialCard.MIRANAGITE.item
    }
    val MIRANAGI_STAFF = !ToolCard(
        "miranagi_staff", 3, "Staff of Miranagi", "みらなぎの杖",
        "Risk of vacuum decay due to anti-entropy", "創世の神光は混沌をも翻す。",
        FairyShootingStaffSettings(ToolMaterialCard.MIRANAGITE, 10F, 16F).silkTouch(),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern(" IG")
            pattern(" #I")
            pattern("N  ")
            input('#', MIRANAGI_STAFF_0.item)
            input('G', Items.DIAMOND)
            input('I', Items.IRON_INGOT)
            input('N', Items.IRON_NUGGET)
        } on MaterialCard.MIRANAGITE.item
    }
    val XARPITE_PICKAXE = !ToolCard(
        "xarpite_pickaxe", 2, "Xarpite Pickaxe", "紅天石のつるはし",
        "Shears space using astral induction", "鉱石の魂を貪る血塗られた有機質。",
        FairyPickaxeSettings(ToolMaterialCard.XARPITE).mineAll(),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("###")
            pattern(" R ")
            pattern(" R ")
            input('#', MaterialCard.XARPITE.item)
            input('R', Items.STICK)
        } on MaterialCard.XARPITE.item
    }
    val XARPITE_AXE = !ToolCard(
        "xarpite_axe", 2, "Xarpite Axe", "紅天石の斧",
        "Strip the log from the space", "空間にこびりついた丸太の除去に。",
        FairyAxeSettings(ToolMaterialCard.XARPITE, 6.0F, -3.1F).cutAll(),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("##")
            pattern("#R")
            pattern(" R")
            input('#', MaterialCard.XARPITE.item)
            input('R', Items.STICK)
        } on MaterialCard.XARPITE.item
    }
    val DIAMOND_SCYTHE = !ToolCard(
        "diamond_scythe", 3, "Diamond Scythe", "ダイヤモンドの大鎌",
        "A highly durable scythe made of diamond.", "ダイヤモンドを加工した高耐久の大鎌。",
        FairyScytheSettings(ToolMaterialCard.DIAMOND, 3),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern(" ##")
            pattern("# R")
            pattern("  R")
            input('#', Items.DIAMOND)
            input('R', MaterialCard.MIRAGE_STEM.item)
        } on Items.DIAMOND
    }
    val CHAOS_STONE_PICKAXE = !ToolCard(
        "chaos_stone_pickaxe", 4, "Chaos Stone Pickaxe", "混沌のつるはし",
        "Is this made of metal? Or clay?", "時空結晶の交点に、古代の産業が芽吹く。",
        FairyPickaxeSettings(ToolMaterialCard.CHAOS_STONE).also { it.effectiveBlockTags += BlockTags.SHOVEL_MINEABLE }.areaMining(),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("###")
            pattern(" R ")
            pattern(" R ")
            input('#', MaterialCard.CHAOS_STONE.item)
            input('R', Items.STICK)
        } on MaterialCard.CHAOS_STONE.item
    }
    val PHANTOM_PICKAXE = !ToolCard(
        "phantom_pickaxe", 4, "Phantom Pickaxe", "幻想のつるはし",
        "\"Creation\" is the true power.", "人間が手にした唯一の幻想。",
        FairyPickaxeSettings(ToolMaterialCard.PHANTOM_DROP).selfMending(20).obtainFairy(9.0 * 9.0),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("###")
            pattern(" R ")
            pattern(" R ")
            input('#', MaterialCard.PHANTOM_DROP.item)
            input('R', Items.STICK)
        } on MaterialCard.PHANTOM_DROP.item
    }
    val PHANTOM_SHOVEL = !ToolCard(
        "phantom_shovel", 4, "Phantom Shovel", "幻想のシャベル",
        "The sound of the world's end echoed", "破壊された世界の音――",
        FairyShovelSettings(ToolMaterialCard.PHANTOM_DROP).selfMending(20).obtainFairy(9.0 * 9.0),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("#")
            pattern("R")
            pattern("R")
            input('#', MaterialCard.PHANTOM_DROP.item)
            input('R', Items.STICK)
        } on MaterialCard.PHANTOM_DROP.item
    }
    val PHANTOM_SWORD = !ToolCard(
        "phantom_sword", 4, "Phantom Sword", "幻想の剣",
        "Pray. For rebirth.", "闇を切り裂く、再生の光。",
        FairySwordSettings(ToolMaterialCard.PHANTOM_DROP).selfMending(20).obtainFairy(9.0 * 9.0),
    ) {
        registerShapedRecipeGeneration(item) {
            pattern("#")
            pattern("#")
            pattern("R")
            input('#', MaterialCard.PHANTOM_DROP.item)
            input('R', Items.STICK)
        } on MaterialCard.PHANTOM_DROP.item
    }
}

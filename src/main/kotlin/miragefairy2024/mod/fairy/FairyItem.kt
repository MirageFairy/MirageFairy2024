package miragefairy2024.mod.fairy

import miragefairy2024.MirageFairy2024
import miragefairy2024.mod.Emoji
import miragefairy2024.mod.invoke
import miragefairy2024.mod.passiveskill.ManaBoostPassiveSkillEffect
import miragefairy2024.mod.passiveskill.PASSIVE_SKILL_TRANSLATION
import miragefairy2024.mod.passiveskill.PassiveSkill
import miragefairy2024.mod.passiveskill.PassiveSkillContext
import miragefairy2024.mod.passiveskill.PassiveSkillEffectCard
import miragefairy2024.mod.passiveskill.PassiveSkillProvider
import miragefairy2024.mod.passiveskill.PassiveSkillResult
import miragefairy2024.mod.passiveskill.PassiveSkillSpecification
import miragefairy2024.mod.passiveskill.PassiveSkillStatus
import miragefairy2024.mod.passiveskill.collect
import miragefairy2024.mod.passiveskill.description
import miragefairy2024.mod.passiveskill.findPassiveSkillProviders
import miragefairy2024.util.ItemGroupCard
import miragefairy2024.util.Model
import miragefairy2024.util.ModelData
import miragefairy2024.util.ModelTexturesData
import miragefairy2024.util.Translation
import miragefairy2024.util.aqua
import miragefairy2024.util.buildText
import miragefairy2024.util.concat
import miragefairy2024.util.createItemStack
import miragefairy2024.util.darkGray
import miragefairy2024.util.enJa
import miragefairy2024.util.eyeBlockPos
import miragefairy2024.util.get
import miragefairy2024.util.gold
import miragefairy2024.util.gray
import miragefairy2024.util.green
import miragefairy2024.util.int
import miragefairy2024.util.invoke
import miragefairy2024.util.red
import miragefairy2024.util.register
import miragefairy2024.util.registerColorProvider
import miragefairy2024.util.registerItemGroup
import miragefairy2024.util.registerItemModelGeneration
import miragefairy2024.util.string
import miragefairy2024.util.text
import miragefairy2024.util.toIdentifier
import miragefairy2024.util.wrapper
import miragefairy2024.util.yellow
import mirrg.kotlin.hydrogen.formatAs
import mirrg.kotlin.hydrogen.or
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.world.World
import kotlin.math.log
import kotlin.math.roundToInt

object FairyCard {
    val enName = "Invalid Fairy"
    val jaName = "無効な妖精"
    val identifier = Identifier(MirageFairy2024.modId, "fairy")
    val item = FairyItem(Item.Settings())
}

private val RARE_TRANSLATION = Translation({ "item.miragefairy2024.fairy.rare" }, "Rare", "レア")
private val MANA_TRANSLATION = Translation({ "item.miragefairy2024.fairy.mana" }, "Mana", "魔力")
private val LEVEL_TRANSLATION = Translation({ "item.miragefairy2024.fairy.level" }, "Level", "レベル")
private val CONDENSATION_TRANSLATION = Translation({ "item.miragefairy2024.fairy.condensation" }, "Condensation", "凝縮数")
private val CONDENSATION_RECIPE_TRANSLATION = Translation({ "item.miragefairy2024.fairy.condensation_recipe" }, "Can be (de)condensed by crafting table", "作業台で凝縮・展開")

val fairiesItemGroupCard = ItemGroupCard(
    Identifier(MirageFairy2024.modId, "fairies"), "Fairies", "妖精",
) { FairyCard.item.createItemStack().also { it.setFairyMotif(MotifCard.MAGENTA_GLAZED_TERRACOTTA) } }

fun initFairyItem() {
    FairyCard.let { card ->
        card.item.register(Registries.ITEM, card.identifier)
        card.item.registerItemGroup(fairiesItemGroupCard.itemGroupKey) {
            motifRegistry.entrySet.sortedBy { it.key.value }.map {
                val itemStack = card.item.createItemStack()
                itemStack.setFairyMotif(it.value)
                itemStack
            }
        }

        card.item.registerItemModelGeneration(createFairyModel())
        card.item.registerColorProvider { itemStack, tintIndex ->
            if (tintIndex == 4) {
                val condensation = itemStack.getFairyCondensation()
                when (getNiceCondensation(condensation).first) {
                    0 -> 0xFF8E8E // 赤
                    1 -> 0xB90000
                    2 -> 0xAAAAFF // 青
                    3 -> 0x0000FF
                    4 -> 0x00D100 // 緑
                    5 -> 0x007A00
                    6 -> 0xFFFF60 // 黄色
                    7 -> 0x919100
                    8 -> 0x00D1D1 // 水色
                    9 -> 0x009E9E
                    10 -> 0xFF87FF // マゼンタ
                    11 -> 0xDB00DB
                    12 -> 0xFFBB77 // オレンジ
                    13 -> 0xCE6700
                    14 -> 0x66FFB2 // 草
                    15 -> 0x00B758
                    16 -> 0xD1A3FF // 紫
                    17 -> 0xA347FF
                    18 -> 0xCECECE // 灰色
                    19 -> 0x919191
                    else -> 0x333333
                }
            } else {
                val motif = itemStack.getFairyMotif() ?: return@registerColorProvider 0xFF00FF
                when (tintIndex) {
                    0 -> motif.skinColor
                    1 -> motif.frontColor
                    2 -> motif.backColor
                    3 -> motif.hairColor
                    else -> 0xFF00FF
                }
            }
        }

        card.item.enJa(card.enName, card.jaName)
    }

    RARE_TRANSLATION.enJa()
    MANA_TRANSLATION.enJa()
    LEVEL_TRANSLATION.enJa()
    CONDENSATION_TRANSLATION.enJa()
    CONDENSATION_RECIPE_TRANSLATION.enJa()

    fairiesItemGroupCard.init()
}

private fun createFairyModel() = Model {
    ModelData(
        parent = Identifier("item/generated"),
        textures = ModelTexturesData(
            "layer0" to Identifier(MirageFairy2024.modId, "item/fairy_skin").string,
            "layer1" to Identifier(MirageFairy2024.modId, "item/fairy_front").string,
            "layer2" to Identifier(MirageFairy2024.modId, "item/fairy_back").string,
            "layer3" to Identifier(MirageFairy2024.modId, "item/fairy_hair").string,
            "layer4" to Identifier(MirageFairy2024.modId, "item/fairy_dress").string,
        ),
    )
}

class FairyItem(settings: Settings) : Item(settings), PassiveSkillProvider {
    override fun getName(stack: ItemStack): Text {
        val originalName = stack.getFairyMotif()?.displayName ?: super.getName(stack)
        val condensation = stack.getFairyCondensation()
        return if (condensation != 1) text { originalName + " x$condensation"() } else originalName
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, tooltip, context)
        val player = MirageFairy2024.clientProxy?.getClientPlayer()
        val motif = stack.getFairyMotif() ?: return

        // 魔力
        val level = motif.rare.toDouble() + log(stack.getFairyCondensation().toDouble() * stack.count, 3.0)
        val (manaBoost, status) = if (player != null) {
            val passiveSkillProviders = player.findPassiveSkillProviders()
            val result = PassiveSkillResult()
            result.collect(passiveSkillProviders.passiveSkills, player, ManaBoostPassiveSkillEffect.Value(mapOf()), true) // 先行判定
            val status = passiveSkillProviders.providers.find { it.first === stack }?.second ?: PassiveSkillStatus.DISABLED
            Pair(result[PassiveSkillEffectCard.MANA_BOOST].map.entries.sumOf { (keyMotif, value) -> if (motif in keyMotif) value else 0.0 }, status)
        } else {
            Pair(0.0, PassiveSkillStatus.DISABLED)
        }
        val mana = level * (1.0 + manaBoost)
        tooltip += text { (MANA_TRANSLATION() + ": "() + Emoji.MANA() + (mana formatAs "%.1f")()).aqua }

        // レベル
        tooltip += text { (LEVEL_TRANSLATION() + ": "() + Emoji.STAR() + (level formatAs "%.1f")()).green }

        // レア・凝縮数
        tooltip += text { (RARE_TRANSLATION() + ": ${motif.rare}"() + "  "() + CONDENSATION_TRANSLATION() + ": x${stack.getFairyCondensation()}"() + if (stack.count != 1) " *${stack.count}"() else empty()).green }

        // 機能説明
        tooltip += text { CONDENSATION_RECIPE_TRANSLATION().yellow }

        // パッシブスキル
        if (motif.passiveSkillSpecifications.isNotEmpty()) {

            tooltip += text { empty() }

            val isEffectiveItemStack = status == PassiveSkillStatus.EFFECTIVE
            tooltip += text { (PASSIVE_SKILL_TRANSLATION() + ": "() + status.description.let { if (status != PassiveSkillStatus.EFFECTIVE) it.red else it }).let { if (isEffectiveItemStack) it.gold else it.gray } }
            val passiveSkillContext = player?.let { PassiveSkillContext(it.world, it.eyeBlockPos, it) }
            motif.passiveSkillSpecifications.forEach { specification ->
                fun <T> getSpecificationText(specification: PassiveSkillSpecification<T>): Text {
                    val actualMana = if (specification.effect.isPreprocessor) level else level * (1.0 + manaBoost)
                    val conditionValidityList = specification.conditions.map { Pair(it, passiveSkillContext != null && it.test(passiveSkillContext, level, mana)) }
                    val isAvailableSpecification = conditionValidityList.all { it.second }
                    return buildText {
                        !text { " "() }
                        !text { specification.effect.getText(specification.valueProvider(actualMana)) }
                        if (conditionValidityList.isNotEmpty()) {
                            !text { " ["() }
                            conditionValidityList.forEachIndexed { index, (condition, isValidCondition) ->
                                if (index != 0) !text { ","() }
                                !text { condition.text }.let { if (!isValidCondition) it.red else it }
                            }
                            !text { "]"() }
                        }
                    }.let { if (isAvailableSpecification) if (isEffectiveItemStack) it.gold else it.gray else it.darkGray }
                }
                tooltip += getSpecificationText(specification)
            }
        }
    }

    override fun isItemBarVisible(stack: ItemStack): Boolean {
        val condensation = stack.getFairyCondensation()
        val niceCondensation = getNiceCondensation(condensation).second
        return condensation != niceCondensation
    }

    override fun getItemBarStep(stack: ItemStack): Int {
        val condensation = stack.getFairyCondensation()
        val niceCondensation = getNiceCondensation(condensation).second.toLong()
        val nextNiceCondensation = niceCondensation * 3L
        return (13.0 * (condensation.toLong() - niceCondensation).toDouble() / (nextNiceCondensation - niceCondensation).toDouble()).roundToInt()
    }

    override fun getItemBarColor(stack: ItemStack) = 0x00FF00

    override fun getPassiveSkill(itemStack: ItemStack): PassiveSkill? {
        val motif = itemStack.getFairyMotif() ?: return null
        val level = motif.rare.toDouble() + log(itemStack.getFairyCondensation().toDouble() * itemStack.count, 3.0)
        return PassiveSkill("fairy/" concat motif.getIdentifier()!!, motif, level, motif.passiveSkillSpecifications)
    }
}

fun ItemStack.getFairyMotifId(): Identifier? = this.nbt.or { return null }.wrapper["FairyMotif"].string.get().or { return null }.toIdentifier()
fun ItemStack.getFairyMotif() = this.getFairyMotifId()?.let { motifRegistry.get(it) }

fun ItemStack.setFairyMotifId(identifier: Identifier) = getOrCreateNbt().wrapper["FairyMotif"].string.set(identifier.string)
fun ItemStack.setFairyMotif(recipe: Motif) = this.setFairyMotifId(motifRegistry.getId(recipe)!!)

fun ItemStack.getFairyCondensation() = this.nbt.or { return 1 }.wrapper["FairyCondensation"].int.get() ?: 1
fun ItemStack.setFairyCondensation(condensation: Int) = getOrCreateNbt().wrapper["FairyCondensation"].int.set(condensation)

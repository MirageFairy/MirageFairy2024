package miragefairy2024.mod.tool

import miragefairy2024.MirageFairy2024
import miragefairy2024.mixin.api.ItemPredicateConvertorCallback
import miragefairy2024.mixin.api.OverrideEnchantmentLevelCallback
import miragefairy2024.mod.AntimatterBoltCard
import miragefairy2024.mod.AntimatterBoltEntity
import miragefairy2024.mod.SoundEventCard
import miragefairy2024.util.Translation
import miragefairy2024.util.invoke
import miragefairy2024.util.randomInt
import miragefairy2024.util.text
import miragefairy2024.util.yellow
import net.minecraft.block.BlockState
import net.minecraft.client.item.TooltipContext
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.item.ToolMaterial
import net.minecraft.item.Vanishable
import net.minecraft.sound.SoundCategory
import net.minecraft.stat.Stats
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class FairyShootingStaffItem(override val fairyToolSettings: FairyToolSettings<FairyShootingStaffItem>, settings: Settings) :
    ShootingStaffItem(fairyToolSettings.toolMaterialCard.toolMaterial, fairyToolSettings.basePower, settings),
    FairyToolItem<FairyShootingStaffItem>,
    OverrideEnchantmentLevelCallback,
    ItemPredicateConvertorCallback {
    override fun getMiningSpeedMultiplier(stack: ItemStack, state: BlockState) = getMiningSpeedMultiplierImpl(this, stack, state)
    override fun isSuitableFor(state: BlockState) = isSuitableForImpl(this, state)

    override fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity): Boolean {
        super.postMine(stack, world, state, pos, miner)
        postMineImpl(this, stack, world, state, pos, miner)
        return true
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, world, entity, slot, selected)
        inventoryTickImpl(this, stack, world, entity, slot, selected)
    }

    override fun overrideEnchantmentLevel(enchantment: Enchantment, itemStack: ItemStack, oldLevel: Int) = overrideEnchantmentLevelImpl(this, enchantment, itemStack, oldLevel)

    override fun convertItemStack(itemStack: ItemStack) = convertItemStackImpl(this, itemStack)
}

open class ShootingStaffItem(toolMaterial: ToolMaterial, private val basePower: Float, settings: Settings) : ToolItem(toolMaterial, settings), Vanishable {
    companion object {
        val NOT_ENOUGH_EXPERIENCE_TRANSLATION = Translation({ "item.${MirageFairy2024.MOD_ID}.fairy_tool_item.not_enough_experience" }, "Not enough experience", "経験値が足りません")
        val DESCRIPTION_TRANSLATION = Translation({ "item.${MirageFairy2024.MOD_ID}.shooting_staff.description" }, "Perform a ranged attack when used", "使用時、射撃攻撃")
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, tooltip, context)
        tooltip += text { DESCRIPTION_TRANSLATION().yellow }
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = user.getStackInHand(hand)
        if (world.isClient) return TypedActionResult.success(itemStack)

        if (!user.isCreative) {
            if (user.totalExperience < 1) {
                user.sendMessage(text { NOT_ENOUGH_EXPERIENCE_TRANSLATION() }, true)
                return TypedActionResult.consume(itemStack)
            }
        }

        val damage = basePower
        val limitDistance = 16.0
        val speed = 4.0F
        val frequency = 0.5

        // 生成
        val entity = AntimatterBoltEntity(AntimatterBoltCard.entityType, world, damage, limitDistance)
        entity.setPosition(user.x, user.eyeY - 0.3, user.z)
        entity.setVelocity(user, user.pitch, user.yaw, 0.0F, speed, 1.0F)
        entity.owner = user
        world.spawnEntity(entity)

        // 消費
        itemStack.damage(1, user) {
            it.sendToolBreakStatus(hand)
        }
        if (!user.isCreative) user.addExperience(-1)

        user.itemCooldownManager.set(this, world.random.randomInt(10.0 / frequency))

        // 統計
        user.incrementStat(Stats.USED.getOrCreateStat(this))

        // エフェクト
        world.playSound(null, user.x, user.y, user.z, SoundEventCard.MAGIC2.soundEvent, SoundCategory.PLAYERS, 1.0F, 0.90F + (world.random.nextFloat() - 0.5F) * 0.3F)

        return TypedActionResult.consume(itemStack)
    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        stack.damage(2, attacker) { e ->
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND)
        }
        return true
    }

    override fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity): Boolean {
        if (state.getHardness(world, pos) != 0.0F) {
            stack.damage(2, miner) { e ->
                e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND)
            }
        }
        return true
    }
}

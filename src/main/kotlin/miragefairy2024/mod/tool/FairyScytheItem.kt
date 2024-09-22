package miragefairy2024.mod.tool

import miragefairy2024.mixin.api.ItemPredicateConvertorCallback
import miragefairy2024.mixin.api.OverrideEnchantmentLevelCallback
import miragefairy2024.mod.magicplant.MagicPlantBlock
import miragefairy2024.mod.magicplant.PostTryPickHandlerItem
import net.minecraft.block.BlockState
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.item.ToolMaterial
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class FairyScytheItem(override val toolSettings: FairyToolSettings, settings: Settings) :
    ScytheItem(toolSettings.toolMaterialCard.toolMaterial, toolSettings.attackDamage, toolSettings.attackSpeed, settings),
    FairyToolItem,
    OverrideEnchantmentLevelCallback,
    ItemPredicateConvertorCallback {

    override fun getMiningSpeedMultiplier(stack: ItemStack, state: BlockState) = getMiningSpeedMultiplierImpl(stack, state)

    override fun isSuitableFor(state: BlockState) = isSuitableForImpl(state)

    override fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity): Boolean {
        super.postMine(stack, world, state, pos, miner)
        postMineImpl(stack, world, state, pos, miner)
        return true
    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        super.postHit(stack, target, attacker)
        postHitImpl(stack, target, attacker)
        return true
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, world, entity, slot, selected)
        inventoryTickImpl(stack, world, entity, slot, selected)
    }

    override fun overrideEnchantmentLevel(enchantment: Enchantment, itemStack: ItemStack, oldLevel: Int) = overrideEnchantmentLevelImpl(enchantment, itemStack, oldLevel)

    override fun convertItemStack(itemStack: ItemStack) = convertItemStackImpl(itemStack)

}

open class ScytheItem(material: ToolMaterial, attackDamage: Float, attackSpeed: Float, settings: Settings) : SwordItem(material, attackDamage.toInt(), attackSpeed, settings), PostTryPickHandlerItem {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        stack.damage(2, attacker) { e ->
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND)
        }
        return true
    }

    override fun postMine(stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity): Boolean {
        if (state.getHardness(world, pos) != 0.0F) {
            if (miner.random.nextFloat() < 0.2F) {
                stack.damage(1, miner) { e ->
                    e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND)
                }
            }
        }
        return true
    }

    override fun postTryPick(world: World, blockPos: BlockPos, player: PlayerEntity?, itemStack: ItemStack, succeed: Boolean) {
        if (world.isClient) return
        (-1..1).forEach { x ->
            (-1..1).forEach { y ->
                (-1..1).forEach { z ->
                    if (x != 0 || y != 0 || z != 0) {
                        val targetBlockPos = blockPos.add(x, y, z)
                        val targetBlockState = world.getBlockState(targetBlockPos)
                        val targetBlock = targetBlockState.block
                        if (targetBlock is MagicPlantBlock) {
                            targetBlock.tryPick(world, targetBlockPos, player, itemStack, true, false)
                        }
                    }
                }
            }
        }
    }
}

package miragefairy2024.mod.fairy

import miragefairy2024.MirageFairy2024
import miragefairy2024.ModContext
import miragefairy2024.mod.sync
import miragefairy2024.util.Translation
import miragefairy2024.util.enJa
import miragefairy2024.util.eyeBlockPos
import miragefairy2024.util.invoke
import miragefairy2024.util.itemStacks
import miragefairy2024.util.opposite
import miragefairy2024.util.registerServerDebugItem
import miragefairy2024.util.sendToClient
import miragefairy2024.util.text
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.ChestBlock
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.entity.EntityType
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.world.RaycastContext
import net.minecraft.world.World

private val identifier = MirageFairy2024.identifier("fairy_dream")
val GAIN_FAIRY_DREAM_TRANSLATION = Translation({ "gui.${identifier.toTranslationKey()}.gain" }, "Dreamed of a new fairy!", "新たな妖精の夢を見た！")
val GAIN_FAIRY_TRANSLATION = Translation({ "gui.${identifier.toTranslationKey()}.gain_fairy" }, "%s found!", "%sを発見した！")

context(ModContext)
fun initFairyDream() {

    // デバッグアイテム
    registerServerDebugItem("debug_clear_fairy_dream", Items.STRING, 0x0000DD) { world, player, _, _ ->
        player.fairyDreamContainer.clear()
        player.sendMessage(text { "Cleared fairy dream"() }, true)
    }
    registerServerDebugItem("debug_gain_fairy_dream", Items.STRING, 0x0000BB) { world, player, hand, _ ->
        val fairyItemStack = player.getStackInHand(hand.opposite)
        if (!fairyItemStack.isOf(FairyCard.item)) return@registerServerDebugItem
        val motif = fairyItemStack.getFairyMotif() ?: return@registerServerDebugItem

        if (!player.isSneaking) {
            player.fairyDreamContainer[motif] = true
            GainFairyDreamChannel.sendToClient(player, motif)
        } else {
            player.fairyDreamContainer[motif] = false
            FairyDreamContainerExtraPlayerDataCategory.sync(player)
        }
    }

    // 妖精の夢回収判定
    ServerTickEvents.END_SERVER_TICK.register { server ->
        if (server.ticks % (20 * 5) == 0) {
            server.playerManager.playerList.forEach { player ->
                if (player.isSpectator) return@forEach
                val world = player.world
                val random = world.random

                val motifs = mutableSetOf<Motif>()

                val items = mutableSetOf<Item>()
                val blocks = mutableSetOf<Block>()
                val entityTypes = mutableSetOf<EntityType<*>>()
                run {

                    fun insertItem(itemStack: ItemStack) {
                        val item = itemStack.item

                        items += item

                        if (item is FairyDreamProviderItem) motifs += item.getFairyDreamMotifs(itemStack)

                        val block = Block.getBlockFromItem(item)
                        if (block != Blocks.AIR) blocks += block

                    }

                    fun insertBlockPos(blockPos: BlockPos) {
                        val blockState = world.getBlockState(blockPos)
                        val block = blockState.block

                        blocks += block

                        if (block is FairyDreamProviderBlock) motifs += block.getFairyDreamMotifs(world, blockPos)

                        run noInventory@{
                            val inventory = if (block is InventoryProvider) {
                                block.getInventory(blockState, world, blockPos)
                            } else if (blockState.hasBlockEntity()) {
                                val blockEntity = world.getBlockEntity(blockPos)
                                if (blockEntity is Inventory) {
                                    if (blockEntity is ChestBlockEntity && block is ChestBlock) {
                                        ChestBlock.getInventory(block, blockState, world, blockPos, true) ?: return@noInventory
                                    } else {
                                        blockEntity
                                    }
                                } else {
                                    return@noInventory
                                }
                            } else {
                                return@noInventory
                            }
                            inventory.itemStacks.forEach { itemStack ->
                                insertItem(itemStack)
                            }
                        }

                    }


                    // インベントリ判定
                    player.inventory.itemStacks.forEach { itemStack ->
                        insertItem(itemStack)
                    }

                    // 足元判定
                    insertBlockPos(player.blockPos)
                    insertBlockPos(player.blockPos.down())

                    // 視線判定
                    val start = player.eyePos
                    val pitch = player.pitch
                    val yaw = player.yaw
                    val d = MathHelper.cos(-yaw * (MathHelper.PI / 180) - MathHelper.PI)
                    val a = MathHelper.sin(-yaw * (MathHelper.PI / 180) - MathHelper.PI)
                    val e = -MathHelper.cos(-pitch * (MathHelper.PI / 180))
                    val c = MathHelper.sin(-pitch * (MathHelper.PI / 180))
                    val end = start.add(a * e * 32.0, c * 32.0, d * e * 32.0)
                    val raycastResult = world.raycast(RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player))
                    if (raycastResult.type == HitResult.Type.BLOCK) insertBlockPos(raycastResult.blockPos)

                    // 周辺エンティティ判定
                    val entities = world.getOtherEntities(player, Box(player.eyePos.add(-8.0, -8.0, -8.0), player.eyePos.add(8.0, 8.0, 8.0)))
                    entities.forEach {
                        entityTypes += it.type
                    }

                    // 周辺ブロック判定
                    insertBlockPos(player.eyeBlockPos.add(random.nextInt(17) - 8, random.nextInt(17) - 8, random.nextInt(17) - 8))

                }
                items.forEach {
                    motifs += FairyDreamRecipes.ITEM.test(it)
                }
                blocks.forEach {
                    motifs += FairyDreamRecipes.BLOCK.test(it)
                }
                entityTypes.forEach {
                    motifs += FairyDreamRecipes.ENTITY_TYPE.test(it)
                }

                player.fairyDreamContainer.gain(player, motifs)

            }
        }
    }

    // 翻訳
    GAIN_FAIRY_DREAM_TRANSLATION.enJa()
    GAIN_FAIRY_TRANSLATION.enJa()

}


interface FairyDreamProviderItem {
    fun getFairyDreamMotifs(itemStack: ItemStack): List<Motif>
}

interface FairyDreamProviderBlock {
    fun getFairyDreamMotifs(world: World, blockPos: BlockPos): List<Motif>
}

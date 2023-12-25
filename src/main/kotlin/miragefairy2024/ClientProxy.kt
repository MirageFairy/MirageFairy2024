package miragefairy2024

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

interface ClientProxy {
    fun registerItemTooltipCallback(block: (stack: ItemStack, lines: MutableList<Text>) -> Unit)
    fun registerCutoutRenderLayer(block: Block)
    fun getClientPlayer(): PlayerEntity?
    fun getBlockColorProvider(block: Block): BlockColorProvider?
    fun registerBlockColorProvider(block: Block, provider: BlockColorProvider)
    fun getFoliageBlockColorProvider(): BlockColorProvider
    fun getItemColorProvider(item: Item): ItemColorProvider?
    fun registerItemColorProvider(item: Item, provider: ItemColorProvider)
}

fun interface BlockColorProvider {
    operator fun invoke(blockState: BlockState, world: BlockView?, blockPos: BlockPos?, tintIndex: Int): Int
}

fun interface ItemColorProvider {
    operator fun invoke(itemStack: ItemStack, tintIndex: Int): Int
}

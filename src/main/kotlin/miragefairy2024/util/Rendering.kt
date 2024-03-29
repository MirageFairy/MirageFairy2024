package miragefairy2024.util

import miragefairy2024.BlockColorProvider
import miragefairy2024.ItemColorProvider
import miragefairy2024.MirageFairy2024
import miragefairy2024.RenderingProxy
import miragefairy2024.RenderingProxyBlockEntity
import mirrg.kotlin.hydrogen.castOrThrow
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

fun Block.registerCutoutRenderLayer() {
    MirageFairy2024.onClientInit {
        it.registerCutoutRenderLayer(this)
    }
}

fun Block.registerColorProvider(provider: BlockColorProvider) = MirageFairy2024.onClientInit {
    it.registerBlockColorProvider(this, provider)
}

fun Block.registerFoliageColorProvider() = this.registerColorProvider { blockState, world, blockPos, tintIndex ->
    MirageFairy2024.clientProxy!!.getFoliageBlockColorProvider().invoke(blockState, world, blockPos, tintIndex)
}

fun Item.registerColorProvider(provider: ItemColorProvider) = MirageFairy2024.onClientInit {
    it.registerItemColorProvider(this, provider)
}

fun BlockItem.registerRedirectColorProvider() = this.registerColorProvider { itemStack, tintIndex ->
    val block = itemStack.item.castOrThrow<BlockItem>().block
    MirageFairy2024.clientProxy!!.getBlockColorProvider(block)!!.invoke(block.defaultState, null, null, tintIndex)
}


fun RenderingProxy.renderItemStack(itemStack: ItemStack, dotX: Double, dotY: Double, dotZ: Double, scale: Float = 1.0F, rotate: Float = 0.0F) {
    this.stack {
        this.translate(dotX / 16.0, dotY / 16.0, dotZ / 16.0)
        this.scale(scale, scale, scale)
        this.rotateY(rotate)
        this.renderItemStack(itemStack)
    }
}

fun <T> BlockEntityType<T>.registerRenderingProxyBlockEntityRendererFactory() where T : BlockEntity, T : RenderingProxyBlockEntity = MirageFairy2024.onClientInit {
    it.registerRenderingProxyBlockEntityRendererFactory(this)
}

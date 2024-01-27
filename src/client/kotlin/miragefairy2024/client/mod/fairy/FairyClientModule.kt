package miragefairy2024.client.mod.fairy

import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.core.Insets
import io.wispforest.owo.ui.core.Sizing
import io.wispforest.owo.ui.core.Surface
import io.wispforest.owo.ui.core.VerticalAlignment
import miragefairy2024.MirageFairy2024
import miragefairy2024.client.util.LimitedLabelComponent
import miragefairy2024.client.util.createOwoToast
import miragefairy2024.client.util.horizontalSpace
import miragefairy2024.client.util.registerClientPacketReceiver
import miragefairy2024.client.util.verticalSpace
import miragefairy2024.mod.fairy.FairyCard
import miragefairy2024.mod.fairy.GAIN_FAIRY_DREAM_TRANSLATION
import miragefairy2024.mod.fairy.GainFairyDreamChannel
import miragefairy2024.mod.fairy.motifTableScreenHandlerType
import miragefairy2024.mod.fairy.setFairyMotif
import miragefairy2024.util.black
import miragefairy2024.util.createItemStack
import miragefairy2024.util.darkBlue
import miragefairy2024.util.invoke
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.util.Identifier

fun initFairyClientModule() {

    // GUI登録
    HandledScreens.register(motifTableScreenHandlerType) { gui, inventory, title -> MotifTableScreen(gui, inventory, title) }

    // パケットハンドラ登録
    GainFairyDreamChannel.registerClientPacketReceiver { motif ->
        val itemStack = FairyCard.item.createItemStack().also { it.setFairyMotif(motif) }
        val component = Containers.horizontalFlow(Sizing.fixed(160), Sizing.fixed(32)).apply {
            surface(Surface.tiled(Identifier(MirageFairy2024.modId, "textures/gui/fairy_dream_toast.png"), 160, 32))
            padding(Insets.of(0, 0, 8, 8))
            verticalAlignment(VerticalAlignment.CENTER)
            child(Components.item(itemStack))
            child(horizontalSpace(6))
            child(Containers.verticalFlow(Sizing.content(), Sizing.content()).apply {
                child(Components.label(GAIN_FAIRY_DREAM_TRANSLATION().black))
                child(verticalSpace(2))
                child(LimitedLabelComponent(itemStack.name.darkBlue).horizontalSizing(Sizing.fixed(160 - 8 - 16 - 6 - 10 - 8)).margins(Insets.of(0, 0, 4, 0)))
            })
        }
        MinecraftClient.getInstance().toastManager.add(createOwoToast(component))
    }

}

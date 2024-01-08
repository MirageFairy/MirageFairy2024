package miragefairy2024.mod.fairyquest

import io.wispforest.owo.ui.base.BaseOwoHandledScreen
import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.Color
import io.wispforest.owo.ui.core.HorizontalAlignment
import io.wispforest.owo.ui.core.Insets
import io.wispforest.owo.ui.core.OwoUIAdapter
import io.wispforest.owo.ui.core.Sizing
import io.wispforest.owo.ui.core.Surface
import io.wispforest.owo.ui.core.VerticalAlignment
import miragefairy2024.MirageFairy2024
import miragefairy2024.mod.NinePatchTextureCard
import miragefairy2024.mod.surface
import miragefairy2024.util.ClickableContainer
import miragefairy2024.util.inventoryNameLabel
import miragefairy2024.util.slotContainer
import miragefairy2024.util.text
import miragefairy2024.util.verticalScroll
import miragefairy2024.util.verticalSpace
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class FairyQuestCardScreen(handler: FairyQuestCardScreenHandler, private val playerInventory: PlayerInventory, title: Text) : BaseOwoHandledScreen<FlowLayout, FairyQuestCardScreenHandler>(handler, playerInventory, title) {
    override fun createAdapter(): OwoUIAdapter<FlowLayout> = OwoUIAdapter.create(this, Containers::verticalFlow)

    private val onScreenUpdate = mutableListOf<() -> Unit>()

    override fun build(rootComponent: FlowLayout) {
        rootComponent.apply {
            surface(Surface.VANILLA_TRANSLUCENT)
            verticalAlignment(VerticalAlignment.CENTER)
            horizontalAlignment(HorizontalAlignment.CENTER)

            // GUI外枠描画用
            child(Containers.verticalFlow(Sizing.content(), Sizing.content()).apply {
                surface(Surface.PANEL)
                padding(Insets.of(7))

                // 横幅固定メインコンテナ
                child(Containers.verticalFlow(Sizing.fixed(18 * 9), Sizing.content()).apply {

                    child(inventoryNameLabel(handler.recipe.title, HorizontalAlignment.CENTER))

                    child(verticalSpace(1))

                    // 本文
                    run {

                        // クリックしたらメッセージを全画面で表示する
                        child(ClickableContainer(Sizing.fill(), Sizing.fixed(11 * 7 + 5), {
                            client!!.setScreen(FairyQuestMessageScreen(this@FairyQuestCardScreen, handler.recipe.title, handler.recipe.message, handler.recipe.client, handler.recipe.title))
                            true
                        }) {

                            // 外枠装飾用パネル
                            Containers.verticalFlow(Sizing.fill(), Sizing.fill()).apply {
                                surface(NinePatchTextureCard.FAIRY_QUEST_CARD_MESSAGE.surface)
                                padding(Insets.of(11, 11, 11, 6))

                                // スクロールコンテナ
                                child(verticalScroll(Sizing.fill(), Sizing.fill(), {
                                    scrollbarThiccness(5)
                                }) {

                                    // スクロールバー回避用パディング設定パネル
                                    Containers.verticalFlow(Sizing.fill(), Sizing.content()).apply {
                                        padding(Insets.of(0, 0, 0, 5))

                                        // 罫線装飾用パネル
                                        child(Containers.verticalFlow(Sizing.fill(), Sizing.content()).apply {
                                            surface(Surface.tiled(Identifier(MirageFairy2024.modId, "textures/gui/fairy_quest_card_line.png"), 11, 11))
                                            padding(Insets.of(0, 1, 0, 0))

                                            // メッセージテキストラベル
                                            child(Components.label(handler.recipe.message).apply {
                                                sizing(Sizing.fill(), Sizing.content())
                                                color(Color.ofRgb(0x6B472E))
                                            })

                                        })

                                    }

                                })

                            }

                        }.apply {
                            tooltip(text { "クリックで全画面表示"()/* TODO */ })
                        })

                    }

                    child(verticalSpace(3))

                    child(inventoryNameLabel(playerInventory.name))

                    child(verticalSpace(1))

                    // プレイヤーインベントリ
                    repeat(3) { r ->
                        child(Containers.horizontalFlow(Sizing.fill(), Sizing.content()).apply {
                            repeat(9) { c ->
                                child(slotContainer(slotAsComponent(9 * r + c)))
                            }
                        })
                    }
                    child(verticalSpace(4))
                    child(Containers.horizontalFlow(Sizing.fill(), Sizing.content()).apply {
                        repeat(9) { c ->
                            child(slotContainer(slotAsComponent(9 * 3 + c)))
                        }
                    })

                })

            })

        }
        onScreenUpdate.forEach {
            it()
        }
    }

    override fun handledScreenTick() {
        super.handledScreenTick()
        onScreenUpdate.forEach {
            it()
        }
    }
}
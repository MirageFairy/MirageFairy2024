package miragefairy2024.mod

import miragefairy2024.MirageFairy2024
import miragefairy2024.util.Translation
import miragefairy2024.util.enJa
import miragefairy2024.util.registerTagGeneration
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

enum class ToolMaterialCard(path: String, enName: String, jaName: String) {
    WOOD("wooden_tool", "Wooden Tool", "木ツール"),
    STONE("stone_tool", "Stone Tool", "石ツール"),
    IRON("iron_tool", "Iron Tool", "鉄ツール"),
    COPPER("copper_tool", "Copper Tool", "銅ツール"),
    GOLD("golden_tool", "Golden Tool", "金ツール"),
    DIAMOND("diamond_tool", "Diamond Tool", "ダイヤモンドツール"),
    NETHERITE("netherite_tool", "Netherite Tool", "ネザライトツール"),
    ;

    val identifier = Identifier(MirageFairy2024.modId, path)
    val tag: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, identifier)
    val translation = Translation({ "${MirageFairy2024.modId}.tool_material.$path" }, enName, jaName)
}

fun initToolMaterialModule() {

    ToolMaterialCard.entries.forEach { card ->
        card.translation.enJa()
    }


    fun register(card: ToolMaterialCard, item: Item) = item.registerTagGeneration { card.tag }

    // WOOD
    register(ToolMaterialCard.WOOD, Items.WOODEN_SWORD)
    register(ToolMaterialCard.WOOD, Items.WOODEN_SHOVEL)
    register(ToolMaterialCard.WOOD, Items.WOODEN_PICKAXE)
    register(ToolMaterialCard.WOOD, Items.WOODEN_AXE)
    register(ToolMaterialCard.WOOD, Items.WOODEN_HOE)
    register(ToolMaterialCard.WOOD, Items.BOW)
    register(ToolMaterialCard.WOOD, Items.CROSSBOW)
    register(ToolMaterialCard.WOOD, Items.FISHING_ROD)
    register(ToolMaterialCard.WOOD, Items.CARROT_ON_A_STICK)
    register(ToolMaterialCard.WOOD, Items.WARPED_FUNGUS_ON_A_STICK)

    // STONE
    register(ToolMaterialCard.STONE, Items.STONE_SWORD)
    register(ToolMaterialCard.STONE, Items.STONE_SHOVEL)
    register(ToolMaterialCard.STONE, Items.STONE_PICKAXE)
    register(ToolMaterialCard.STONE, Items.STONE_AXE)
    register(ToolMaterialCard.STONE, Items.STONE_HOE)

    // IRON
    register(ToolMaterialCard.IRON, Items.IRON_SWORD)
    register(ToolMaterialCard.IRON, Items.IRON_SHOVEL)
    register(ToolMaterialCard.IRON, Items.IRON_PICKAXE)
    register(ToolMaterialCard.IRON, Items.IRON_AXE)
    register(ToolMaterialCard.IRON, Items.IRON_HOE)
    register(ToolMaterialCard.IRON, Items.FLINT_AND_STEEL)
    register(ToolMaterialCard.IRON, Items.SHEARS)

    // COPPER
    register(ToolMaterialCard.COPPER, Items.SPYGLASS)
    register(ToolMaterialCard.COPPER, Items.BRUSH)
    register(ToolMaterialCard.COPPER, Items.TRIDENT)

    // GOLD
    register(ToolMaterialCard.GOLD, Items.GOLDEN_SWORD)
    register(ToolMaterialCard.GOLD, Items.GOLDEN_SHOVEL)
    register(ToolMaterialCard.GOLD, Items.GOLDEN_PICKAXE)
    register(ToolMaterialCard.GOLD, Items.GOLDEN_AXE)
    register(ToolMaterialCard.GOLD, Items.GOLDEN_HOE)

    // DIAMOND
    register(ToolMaterialCard.DIAMOND, Items.DIAMOND_SWORD)
    register(ToolMaterialCard.DIAMOND, Items.DIAMOND_SHOVEL)
    register(ToolMaterialCard.DIAMOND, Items.DIAMOND_PICKAXE)
    register(ToolMaterialCard.DIAMOND, Items.DIAMOND_AXE)
    register(ToolMaterialCard.DIAMOND, Items.DIAMOND_HOE)

    // NETHERITE
    register(ToolMaterialCard.NETHERITE, Items.NETHERITE_SWORD)
    register(ToolMaterialCard.NETHERITE, Items.NETHERITE_SHOVEL)
    register(ToolMaterialCard.NETHERITE, Items.NETHERITE_PICKAXE)
    register(ToolMaterialCard.NETHERITE, Items.NETHERITE_AXE)
    register(ToolMaterialCard.NETHERITE, Items.NETHERITE_HOE)

}

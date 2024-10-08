package miragefairy2024.util

import miragefairy2024.ModContext
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

context(ModContext)
fun Item.registerItemGroup(itemGroup: RegistryKey<ItemGroup>) {
    ItemGroupEvents.modifyEntriesEvent(itemGroup).register {
        it.add(this)
    }
}

context(ModContext)
@Suppress("UnusedReceiverParameter")
fun Item.registerItemGroup(itemGroup: RegistryKey<ItemGroup>, supplier: () -> List<ItemStack>) {
    ItemGroupEvents.modifyEntriesEvent(itemGroup).register {
        supplier().forEach { itemStack ->
            it.add(itemStack)
        }
    }
}


class ItemGroupCard(
    val identifier: Identifier,
    val enName: String,
    val jaName: String,
    icon: () -> ItemStack,
) {
    val translation = Translation({ "itemGroup.${identifier.toTranslationKey()}" }, enName, jaName)
    val itemGroupKey = RegistryKeys.ITEM_GROUP with identifier
    val itemGroup: ItemGroup = FabricItemGroup.builder()
        .icon(icon)
        .displayName(text { translation() })
        .build()

    context(ModContext)
    fun init() {
        itemGroup.register(Registries.ITEM_GROUP, identifier)
        translation.enJa()
    }
}

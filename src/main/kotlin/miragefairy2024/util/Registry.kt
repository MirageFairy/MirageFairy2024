package miragefairy2024.util

import miragefairy2024.ModContext
import miragefairy2024.ModEvents
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

context(ModContext)
fun <T> T.register(registry: Registry<T>, identifier: Identifier) {
    ModEvents.onRegistration {
        Registry.register(registry, identifier, this@register)
    }
}

val <T> Registry<T>.sortedEntrySet: List<Map.Entry<RegistryKey<T>, T>> get() = this.entrySet.sortedBy { it.key.value }

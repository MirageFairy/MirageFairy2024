package miragefairy2024.mod.fairy

import miragefairy2024.MirageFairy2024
import miragefairy2024.mod.ExtraPlayerDataCategory
import miragefairy2024.mod.extraPlayerDataCategoryRegistry
import miragefairy2024.mod.extraPlayerDataContainer
import miragefairy2024.util.boolean
import miragefairy2024.util.get
import miragefairy2024.util.register
import miragefairy2024.util.sendToClient
import miragefairy2024.util.string
import miragefairy2024.util.toIdentifier
import miragefairy2024.util.wrapper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

fun initFairyDreamContainer() {
    FairyDreamContainerExtraPlayerDataCategory.register(extraPlayerDataCategoryRegistry, Identifier(MirageFairy2024.modId, "fairy_dream"))
}

object FairyDreamContainerExtraPlayerDataCategory : ExtraPlayerDataCategory<FairyDreamContainer> {
    override fun create() = FairyDreamContainer()
    override fun castOrThrow(value: Any) = value as FairyDreamContainer
    override val ioHandler = object : ExtraPlayerDataCategory.IoHandler<FairyDreamContainer> {
        override fun fromNbt(player: PlayerEntity, nbt: NbtCompound): FairyDreamContainer {
            val data = FairyDreamContainer()
            nbt.keys.forEach { key ->
                val motif = motifRegistry[key.toIdentifier()] ?: return@forEach
                data[motif] = nbt.wrapper[key].boolean.get() ?: false
            }
            return data
        }

        override fun toNbt(player: PlayerEntity, data: FairyDreamContainer): NbtCompound {
            val nbt = NbtCompound()
            data.entries.forEach { motif ->
                nbt.wrapper[motif.getIdentifier()!!.string].boolean.set(true)
            }
            return nbt
        }
    }
}

val PlayerEntity.fairyDreamContainer get() = this.extraPlayerDataContainer.getOrInit(FairyDreamContainerExtraPlayerDataCategory)

class FairyDreamContainer {

    private val map = mutableSetOf<Motif>()

    operator fun get(motif: Motif) = motif in map

    val entries: Set<Motif> get() = map

    operator fun set(motif: Motif, value: Boolean) {
        if (value) {
            map += motif
        } else {
            map.remove(motif)
        }
    }

    fun gain(player: ServerPlayerEntity, motifs: Iterable<Motif>) {
        (motifs - map).forEach { motif ->
            set(motif, true)
            GainFairyDreamChannel.sendToClient(player, motif)
        }
    }

    fun clear() = map.clear()

}

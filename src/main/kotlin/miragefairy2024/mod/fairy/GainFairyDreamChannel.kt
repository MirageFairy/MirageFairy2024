package miragefairy2024.mod.fairy

import miragefairy2024.MirageFairy2024
import miragefairy2024.util.Channel
import miragefairy2024.util.string
import miragefairy2024.util.toIdentifier
import net.minecraft.network.PacketByteBuf

object GainFairyDreamChannel : Channel<Motif>(MirageFairy2024.identifier("gain_fairy_dream")) {
    override fun writeToBuf(buf: PacketByteBuf, packet: Motif) {
        buf.writeString(packet.getIdentifier()!!.string)
    }

    override fun readFromBuf(buf: PacketByteBuf): Motif {
        val motifId = buf.readString()
        return motifRegistry.get(motifId.toIdentifier())!!
    }
}

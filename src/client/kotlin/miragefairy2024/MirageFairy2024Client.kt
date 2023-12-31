package miragefairy2024

import net.fabricmc.api.ClientModInitializer

object MirageFairy2024Client : ClientModInitializer {
    override fun onInitializeClient() {
        val clientProxy = ClientProxyImpl()
        MirageFairy2024.clientProxy = clientProxy
        MirageFairy2024.onClientInit.fire { it(clientProxy) }
    }
}

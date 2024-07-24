package miragefairy2024

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object ModEvents {
    val onRegistration = InitializationEventRegistry<() -> Unit>()
    val onInitialize = InitializationEventRegistry<() -> Unit>()

    val onClientInit = InitializationEventRegistry<() -> Unit>()
}

object MirageFairy2024 : ModInitializer {
    val modId = "miragefairy2024"
    val logger = LoggerFactory.getLogger("miragefairy2024")
    override fun onInitialize() {
        Modules.init()
        ModEvents.onRegistration.fire { it() }
        ModEvents.onInitialize.fire { it() }
    }
}

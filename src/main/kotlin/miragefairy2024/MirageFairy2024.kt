package miragefairy2024

import miragefairy2024.mod.haimeviska.initHaimeviskaModule
import miragefairy2024.mod.initBlockMaterialsModule
import miragefairy2024.mod.initCommonModule
import miragefairy2024.mod.initMaterialsModule
import miragefairy2024.mod.initNinePatchTextureModule
import miragefairy2024.mod.initOresModule
import miragefairy2024.mod.initPoemModule
import miragefairy2024.mod.initReiModule
import miragefairy2024.mod.initStatusEffectModule
import miragefairy2024.mod.initVanillaModule
import miragefairy2024.mod.magicplant.initMagicPlantModule
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object MirageFairy2024 : ModInitializer {
    val modId = "miragefairy2024"
    val logger = LoggerFactory.getLogger("miragefairy2024")

    val onClientInit = InitializationEventRegistry<(ClientProxy) -> Unit>()
    var clientProxy: ClientProxy? = null

    override fun onInitialize() {
        initCommonModule()
        initVanillaModule()
        initReiModule()
        initPoemModule()
        initStatusEffectModule()
        initMaterialsModule()
        initBlockMaterialsModule()
        initOresModule()
        initMagicPlantModule()
        initHaimeviskaModule()
        initNinePatchTextureModule()
    }
}

class InitializationEventRegistry<T> {
    private val list = mutableListOf<T>()
    private var closed = false

    operator fun invoke(listener: T) {
        require(!closed)
        this.list += listener
    }

    fun fire(processor: (T) -> Unit) {
        closed = true
        this.list.forEach {
            processor(it)
        }
    }
}

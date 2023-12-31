package miragefairy2024.util

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.world.biome.Biome

enum class TemperatureCategory {
    HIGH,
    MEDIUM,
    LOW,
}

val RegistryEntry<Biome>.temperatureCategory
    get() = when {
        this.isIn(ConventionalBiomeTags.CLIMATE_HOT) -> TemperatureCategory.HIGH
        this.isIn(ConventionalBiomeTags.CLIMATE_COLD) -> TemperatureCategory.LOW
        this.isIn(ConventionalBiomeTags.AQUATIC_ICY) -> TemperatureCategory.LOW
        else -> TemperatureCategory.MEDIUM
    }

enum class HumidityCategory {
    HIGH,
    MEDIUM,
    LOW,
}

val RegistryEntry<Biome>.humidityCategory
    get() = when {
        this.isIn(ConventionalBiomeTags.CLIMATE_WET) -> HumidityCategory.HIGH
        this.isIn(ConventionalBiomeTags.CLIMATE_DRY) -> HumidityCategory.LOW
        else -> HumidityCategory.MEDIUM
    }

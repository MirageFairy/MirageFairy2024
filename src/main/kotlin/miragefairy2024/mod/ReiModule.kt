package miragefairy2024.mod

import miragefairy2024.ReiCategoryCard
import miragefairy2024.util.enJa

fun initReiModule() {
    ReiCategoryCard.entries.forEach { card ->
        card.translation.enJa()
    }
}

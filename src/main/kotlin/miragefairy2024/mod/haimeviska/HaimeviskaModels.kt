package miragefairy2024.mod.haimeviska

import miragefairy2024.util.Model
import miragefairy2024.util.ModelData
import miragefairy2024.util.ModelElementData
import miragefairy2024.util.ModelElementsData
import miragefairy2024.util.ModelFaceData
import miragefairy2024.util.ModelFacesData
import miragefairy2024.util.ModelTexturesData
import miragefairy2024.util.concat
import miragefairy2024.util.string
import net.minecraft.data.client.TextureKey
import net.minecraft.util.Identifier

fun createHaimeviskaLeavesModel(identifier: Identifier) = Model {
    ModelData(
        parent = Identifier("minecraft", "block/block"),
        textures = ModelTexturesData(
            TextureKey.PARTICLE.name to TextureKey.BACK.string,
            TextureKey.BACK.name to ("block/" concat identifier).string,
            TextureKey.FRONT.name to ("block/" concat identifier concat "_blossom").string,
        ),
        elements = ModelElementsData(
            ModelElementData(
                from = listOf(0, 0, 0),
                to = listOf(16, 16, 16),
                faces = ModelFacesData(
                    down = ModelFaceData(uv = listOf(0, 0, 16, 16), texture = TextureKey.BACK.string, tintindex = 0, cullface = "down"),
                    up = ModelFaceData(uv = listOf(0, 0, 16, 16), texture = TextureKey.BACK.string, tintindex = 0, cullface = "up"),
                    north = ModelFaceData(uv = listOf(0, 0, 16, 16), texture = TextureKey.BACK.string, tintindex = 0, cullface = "north"),
                    south = ModelFaceData(uv = listOf(0, 0, 16, 16), texture = TextureKey.BACK.string, tintindex = 0, cullface = "south"),
                    west = ModelFaceData(uv = listOf(0, 0, 16, 16), texture = TextureKey.BACK.string, tintindex = 0, cullface = "west"),
                    east = ModelFaceData(uv = listOf(0, 0, 16, 16), texture = TextureKey.BACK.string, tintindex = 0, cullface = "east"),
                ),
            ),
            ModelElementData(
                from = listOf(0, 0, 0),
                to = listOf(16, 16, 16),
                faces = ModelFacesData(
                    down = ModelFaceData(uv = listOf(0, 0, 16, 16), texture = TextureKey.FRONT.string, cullface = "down"),
                    up = ModelFaceData(uv = listOf(0, 0, 16, 16), texture = TextureKey.FRONT.string, cullface = "up"),
                    north = ModelFaceData(uv = listOf(0, 0, 16, 16), texture = TextureKey.FRONT.string, cullface = "north"),
                    south = ModelFaceData(uv = listOf(0, 0, 16, 16), texture = TextureKey.FRONT.string, cullface = "south"),
                    west = ModelFaceData(uv = listOf(0, 0, 16, 16), texture = TextureKey.FRONT.string, cullface = "west"),
                    east = ModelFaceData(uv = listOf(0, 0, 16, 16), texture = TextureKey.FRONT.string, cullface = "east"),
                ),
            ),
        ),
    )
}

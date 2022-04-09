package teksturepako.greenery.common.block.flower

import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary
import teksturepako.greenery.Greenery
import teksturepako.greenery.ModConfig

class BlockLilyOfTheValley : AbstractFlower(NAME, ModConfig.LilyOfTheValley) {
    companion object {
        const val NAME = "lily_of_the_valley"
        const val REGISTRY_NAME = "${Greenery.MODID}:$NAME"
    }

    override fun isBiomeValid(biome: Biome): Boolean {
        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) ||
                BiomeDictionary.hasType(biome, BiomeDictionary.Type.DENSE)
    }
}
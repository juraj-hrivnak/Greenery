package teksturepako.greenery.common.block.flower

import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary
import teksturepako.greenery.Greenery
import teksturepako.greenery.ModConfig

class BlockCornflower : AbstractFlower(NAME, ModConfig.Cornflower) {
    companion object {
        const val NAME = "cornflower"
        const val REGISTRY_NAME = "${Greenery.MODID}:$NAME"
    }

    override fun isBiomeValid(biome: Biome): Boolean {
        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.DENSE) ||
                BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS)

    }
}
package teksturepako.greenery.common.util

import biomesoplenty.api.biome.BOPBiomes
import biomesoplenty.api.biome.IExtendedBiome
import biomesoplenty.common.biome.vanilla.ExtendedBiomeWrapper
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.fml.common.Loader
import java.util.*

object WorldGenUtil
{
    fun removeUnwantedBopGenerators(world: World)
    {
        if (!Loader.isModLoaded("biomesoplenty") || world.isRemote) return

        for (biome in BOPBiomes.REG_INSTANCE.presentBiomes)
        {
            getExtendedBiome(biome).generationManager.removeGenerator("grass")
            getExtendedBiome(biome).generationManager.removeGenerator("ferns")
            getExtendedBiome(biome).generationManager.removeGenerator("double_fern")
            getExtendedBiome(biome).generationManager.removeGenerator("doublegrass")
            getExtendedBiome(biome).generationManager.removeGenerator("barley")
        }
    }

    private fun getExtendedBiome(biome: Biome): IExtendedBiome = BOPBiomes.REG_INSTANCE.getExtendedBiome(biome) ?: let {
        ExtendedBiomeWrapper(biome).run {
            BOPBiomes.REG_INSTANCE.registerBiome(
                this, baseBiome.biomeName.lowercase(Locale.getDefault())
            )
        }
    }

    fun getBiomeInChunk(world: World, chunkX: Int, chunkZ: Int): Biome = world.getBiomeForCoordsBody(
        BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8)
    )
}

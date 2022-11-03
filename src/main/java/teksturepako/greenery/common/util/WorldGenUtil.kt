package teksturepako.greenery.common.util

import biomesoplenty.api.biome.BOPBiomes
import biomesoplenty.api.biome.IExtendedBiome
import biomesoplenty.common.biome.vanilla.ExtendedBiomeWrapper
import net.minecraft.block.material.Material
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.fml.common.Loader

object WorldGenUtil
{

    fun removeBOPGenerators(world: World)
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

    private fun getExtendedBiome(biome: Biome): IExtendedBiome
    {
        var extendedBiome = BOPBiomes.REG_INSTANCE.getExtendedBiome(biome)
        if (extendedBiome == null)
        {
            extendedBiome = ExtendedBiomeWrapper(biome)
            BOPBiomes.REG_INSTANCE.registerBiome(extendedBiome, extendedBiome.getBaseBiome().biomeName.toLowerCase())
        }
        return extendedBiome
    }

    fun getBiomeInChunk(world: World, chunkX: Int, chunkZ: Int): Biome
    {
        return world.getBiomeForCoordsBody(BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8))
    }

    fun canSeeSky(world: World, pos: BlockPos): Boolean
    {
        var topPos = pos
        while (world.getBlockState(topPos).material == Material.WATER)
        {
            topPos = topPos.up()
        }
        if (world.isAirBlock(topPos) || world.getBlockState(topPos).material == Material.PLANTS)
        {
            return world.canSeeSky(topPos)
        }
        return false
    }

    fun areBiomeTypesValid(biome: Biome, types: MutableList<String>, inverted: Boolean): Boolean
    {
        for (type in types)
        {
            if (!inverted)
            {
                while (BiomeDictionary.hasType(biome, BiomeDictionary.Type.getType(type))) return true
            }
            else
            {
                while (BiomeDictionary.hasType(biome, BiomeDictionary.Type.getType(type))) return false
            }
        }
        return if (!inverted) types.isEmpty() else types.isNotEmpty()
    }
}

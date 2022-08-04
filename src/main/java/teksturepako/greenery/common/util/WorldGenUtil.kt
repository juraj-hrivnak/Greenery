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

object WorldGenUtil {

    fun removeBOPGenerators(world: World) {
        if (!Loader.isModLoaded("biomesoplenty") || world.isRemote) return

        for (biome in BOPBiomes.REG_INSTANCE.presentBiomes) {
            getExtendedBiome(biome).generationManager.removeGenerator("grass")
            getExtendedBiome(biome).generationManager.removeGenerator("ferns")
            getExtendedBiome(biome).generationManager.removeGenerator("double_fern")
            getExtendedBiome(biome).generationManager.removeGenerator("doublegrass")
            getExtendedBiome(biome).generationManager.removeGenerator("barley")
        }
    }

    private fun getExtendedBiome(biome: Biome): IExtendedBiome {
        var extendedBiome = BOPBiomes.REG_INSTANCE.getExtendedBiome(biome)
        if (extendedBiome == null) {
            extendedBiome = ExtendedBiomeWrapper(biome)
            BOPBiomes.REG_INSTANCE.registerBiome(extendedBiome, extendedBiome.getBaseBiome().biomeName.toLowerCase())
        }
        return extendedBiome
    }

    fun getBiomeInChunk(world: World, chunkX: Int, chunkZ: Int): Biome {
        return world.getBiomeForCoordsBody(BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8))
    }

    fun canSeeSky(world: World, pos: BlockPos): Boolean {
        var topPos = pos
        while (world.getBlockState(topPos).material == Material.WATER) {
            topPos = topPos.up()
        }
        if (world.isAirBlock(topPos) || world.getBlockState(topPos).material == Material.PLANTS) {
            return world.canSeeSky(topPos)
        }
        return false
    }

    fun areBiomeTypesValid(biome: Biome, types: MutableList<String>, inverted: Boolean): Boolean {
        for (type in types) {
            if (!inverted) {
                while (BiomeDictionary.hasType(biome, getBiomeTypesFromString(type))) return true
            } else {
                while (BiomeDictionary.hasType(biome, getBiomeTypesFromString(type))) return false
            }
        }
        return if (!inverted) types.isEmpty() else types.isNotEmpty()
    }

    private fun getBiomeTypesFromString(type: String): BiomeDictionary.Type {
        return when (type) {
            "HOT" -> BiomeDictionary.Type.HOT
            "COLD" -> BiomeDictionary.Type.COLD
            "SPARSE" -> BiomeDictionary.Type.SPARSE
            "DENSE" -> BiomeDictionary.Type.DENSE
            "WET" -> BiomeDictionary.Type.WET
            "DRY" -> BiomeDictionary.Type.DRY
            "SAVANNA" -> BiomeDictionary.Type.SAVANNA
            "CONIFEROUS" -> BiomeDictionary.Type.CONIFEROUS
            "JUNGLE" -> BiomeDictionary.Type.JUNGLE
            "SPOOKY" -> BiomeDictionary.Type.SPOOKY
            "DEAD" -> BiomeDictionary.Type.DEAD
            "LUSH" -> BiomeDictionary.Type.LUSH
            "NETHER" -> BiomeDictionary.Type.NETHER
            "END" -> BiomeDictionary.Type.END
            "MUSHROOM" -> BiomeDictionary.Type.MUSHROOM
            "MAGICAL" -> BiomeDictionary.Type.MAGICAL
            "RARE" -> BiomeDictionary.Type.RARE
            "OCEAN" -> BiomeDictionary.Type.OCEAN
            "RIVER" -> BiomeDictionary.Type.RIVER
            "WATER" -> BiomeDictionary.Type.WATER
            "MESA" -> BiomeDictionary.Type.MESA
            "FOREST" -> BiomeDictionary.Type.FOREST
            "PLAINS" -> BiomeDictionary.Type.PLAINS
            "MOUNTAIN" -> BiomeDictionary.Type.MOUNTAIN
            "HILLS" -> BiomeDictionary.Type.HILLS
            "SWAMP" -> BiomeDictionary.Type.SWAMP
            "SANDY" -> BiomeDictionary.Type.SANDY
            "SNOWY" -> BiomeDictionary.Type.SNOWY
            "WASTELAND" -> BiomeDictionary.Type.WASTELAND
            "BEACH" -> BiomeDictionary.Type.BEACH
            "VOID" -> BiomeDictionary.Type.VOID
            else -> {
                BiomeDictionary.Type.VOID
            }
        }
    }
}

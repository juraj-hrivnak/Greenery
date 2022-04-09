package teksturepako.greenery.common.util

import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary

object WorldGenUtil {
    fun getBiomeInChunk(world: World, chunkX: Int, chunkZ: Int): Biome {
        return world.getBiomeForCoordsBody(BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8))
    }

    fun canSeeSky(world: World, pos: BlockPos): Boolean {
        var topPos = pos
        while (world.getBlockState(topPos).material == Material.WATER) {
            topPos = topPos.up()
        }
        val block = world.getBlockState(topPos).block
        if (world.isAirBlock(topPos) || block == Blocks.ICE || block == Blocks.WATERLILY) {
            return world.canSeeSky(topPos)
        }
        return false
    }

    fun areBiomeTypesValid(biome: Biome, types: Array<String>): Boolean {
        for (type in types) {
            return BiomeDictionary.hasType(biome, getBiomeTypesFromString(type))
        }
        return types.isEmpty()
    }

    private fun getBiomeTypesFromString(type: String): BiomeDictionary.Type {
        return when (type.toUpperCase()) {
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
            else -> BiomeDictionary.Type.VOID
        }
    }

}

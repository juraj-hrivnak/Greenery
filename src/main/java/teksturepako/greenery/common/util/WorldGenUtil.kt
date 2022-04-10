package teksturepako.greenery.common.util

import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary
import teksturepako.greenery.Greenery.logger

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

    fun parseValidBiomeTypes(types: MutableList<String>) {
        val validTypes = arrayOf(
            "HOT",
            "COLD",
            "SPARSE",
            "DENSE",
            "WET",
            "DRY",
            "SAVANNA",
            "CONIFEROUS",
            "JUNGLE",
            "SPOOKY",
            "DEAD",
            "LUSH",
            "NETHER",
            "END",
            "MUSHROOM",
            "MAGICAL",
            "RARE",
            "OCEAN",
            "RIVER",
            "WATER",
            "MESA",
            "FOREST",
            "PLAINS",
            "MOUNTAIN",
            "HILLS",
            "SWAMP",
            "SANDY",
            "SNOWY",
            "WASTELAND",
            "BEACH",
            "VOID"
        )

        for (type in types) {
            if (type !in validTypes) {
                logger.warn("   ├── Typo in \"valid biome dictionary types\" config:")
                logger.warn("   └── \"$type\"")
            }
        }
    }

}

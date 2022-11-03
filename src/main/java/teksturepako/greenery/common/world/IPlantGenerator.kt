package teksturepako.greenery.common.world

import net.minecraft.block.Block
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.IWorldGenerator
import java.util.*

interface IPlantGenerator : IWorldGenerator
{
    /** The block to be generated. */
    val block: Block

    /** The chance to attempt to generate in a given chunk. */
    val generationChance: Double

    /** Attempts to generate a patch in a given chunk. */
    val patchAttempts: Int

    /** Attempts to generate a plant in every patch. */
    val plantAttempts: Int

    /** A list of biome dictionary types in which a plant can generate. */
    val validBiomeTypes: MutableList<String>

    /** Whether Valid Biome Dictionary Types are inverted. */
    val inverted: Boolean

    /** Plant generator */
    fun generatePlants(world: World, rand: Random, targetPos: BlockPos)
}
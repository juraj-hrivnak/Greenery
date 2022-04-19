package teksturepako.greenery.api.world

import net.minecraft.block.Block
import net.minecraftforge.fml.common.IWorldGenerator
import teksturepako.greenery.common.block.GreeneryCropBase

interface IGreeneryWorldGenerator : IWorldGenerator {

    /** The block to be generated. */
    val block: Block

    /** The chance to attempt generating in a given chunk. */
    val generationChance: Double

    /** Attempts to generate a patch in a given chunk. */
    val patchAttempts: Int

    /** Attempts to generate a plant in every patch. */
    val plantAttempts: Int

    /** A list of biome dictionary types in which a plant can generate. */
    val validBiomeTypes: MutableList<String>

    /** Whether Valid Biome Dictionary Types are inverted. */
    val inverted: Boolean

}
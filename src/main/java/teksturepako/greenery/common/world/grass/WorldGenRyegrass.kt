package teksturepako.greenery.common.world.grass

import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.registry.ModBlocks

class WorldGenRyegrass : GrassGenerator()
{
    override val block = ModBlocks.blockRyegrass
    private val config = Config.generation.ryegrass

    override val generationChance = config.generationChance
    override val patchAttempts = config.patchAttempts
    override val plantAttempts = config.plantAttempts
    override val validBiomeTypes = config.validBiomeTypes.toMutableList()
    override val inverted = config.inverted
}


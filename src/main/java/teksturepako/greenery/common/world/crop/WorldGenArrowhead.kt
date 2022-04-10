package teksturepako.greenery.common.world.crop

import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.world.GreeneryWorldGenerator
import teksturepako.greenery.config.Config

class WorldGenArrowhead : GreeneryWorldGenerator() {

    override val block = ModBlocks.blockArrowhead
    private val config = Config.generation.arrowhead

    override val generationChance = config.generationChance
    override val patchAttempts = config.patchAttempts
    override val plantAttempts = config.plantAttempts
    override val validBiomeTypes = config.validBiomeTypes.toMutableList()
    override val inverted = config.inverted

}

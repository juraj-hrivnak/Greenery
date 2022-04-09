package teksturepako.greenery.common.world.crop

import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.world.GreeneryWorldGenerator
import teksturepako.greenery.config.Config

class WorldGenCattail : GreeneryWorldGenerator() {

    override val block = ModBlocks.blockCattail
    private val config = Config.generation.cattail

    override val generationChance = config.generationChance
    override val patchAttempts = config.patchAttempts
    override val plantAttempts = config.plantAttempts
    override val validBiomeTypes = config.validBiomeTypes

}

package teksturepako.greenery.common.world.gen.plant.upland.tall

import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.world.gen.plant.upland.AbstractTallPlantGenerator

class WorldGenNettle : AbstractTallPlantGenerator()
{
    override val block = ModBlocks.blockNettle
    private val config = Config.plant.upland.tall.nettle

    override val generationChance = config.generationChance
    override val patchAttempts = config.patchAttempts
    override val plantAttempts = config.plantAttempts
    override val validBiomeTypes = config.validBiomeTypes.toMutableList()
    override val inverted = config.inverted
}


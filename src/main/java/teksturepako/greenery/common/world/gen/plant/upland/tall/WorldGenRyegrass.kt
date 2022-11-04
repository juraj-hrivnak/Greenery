package teksturepako.greenery.common.world.gen.plant.upland.tall

import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.world.gen.plant.upland.AbstractTallPlantGenerator

class WorldGenRyegrass : AbstractTallPlantGenerator()
{
    override val block = ModBlocks.blockRyegrass
    private val config = Config.plant.upland.tall.ryegrass

    override val generationChance = config.generationChance
    override val patchAttempts = config.patchAttempts
    override val plantAttempts = config.plantAttempts
    override val validBiomeTypes = config.validBiomeTypes.toMutableList()
    override val inverted = config.inverted
}


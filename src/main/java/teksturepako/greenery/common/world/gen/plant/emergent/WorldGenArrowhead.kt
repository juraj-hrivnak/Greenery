package teksturepako.greenery.common.world.gen.plant.emergent

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.world.gen.AbstractPlantGenerator
import java.util.*

class WorldGenArrowhead : AbstractPlantGenerator()
{
    override val block = ModBlocks.blockArrowhead
    private val config = Config.plant.emergent.arrowhead

    override val generationChance = config.generationChance
    override val patchAttempts = config.patchAttempts
    override val plantAttempts = config.plantAttempts
    override val validBiomeTypes = config.validBiomeTypes.toMutableList()
    override val inverted = config.inverted

    override fun placePlant(world: World, pos: BlockPos, rand: Random)
    {
        val startingAge = rand.nextInt(block.maxAge)
        val state = block.defaultState.withProperty(block.ageProperty, startingAge)

        if (block.canBlockStay(world, pos, state))
        {
            world.setBlockState(pos, state, 2)
        }
    }

}

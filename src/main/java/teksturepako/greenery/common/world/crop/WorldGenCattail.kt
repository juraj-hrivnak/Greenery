package teksturepako.greenery.common.world.crop

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.world.GreeneryWorldGenerator
import teksturepako.greenery.config.Config
import java.util.*

class WorldGenCattail : GreeneryWorldGenerator() {

    override val block = ModBlocks.blockCattail
    private val config = Config.generation.cattail

    override val generationChance = config.generationChance
    override val patchAttempts = config.patchAttempts
    override val plantAttempts = config.plantAttempts
    override val validBiomeTypes = config.validBiomeTypes.toMutableList()
    override val inverted = config.inverted

    override fun placePlant(world: World, pos: BlockPos, rand: Random) {
        val startingAge = rand.nextInt(block.maxAge)
        val state = block.defaultState.withProperty(block.ageProperty, startingAge)

        if (block.canBlockStay(world, pos, state)) {
            world.setBlockState(pos, state, 2)
        }
    }

}

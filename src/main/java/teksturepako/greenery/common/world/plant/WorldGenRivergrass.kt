package teksturepako.greenery.common.world.plant

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.util.Constants
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.util.FluidUtil
import teksturepako.greenery.common.world.GreeneryWorldGenerator
import java.util.*

class WorldGenRivergrass : GreeneryWorldGenerator() {

    override val block = ModBlocks.blockRivergrass
    private val config = Config.generation.rivergrass

    override val generationChance = config.generationChance
    override val patchAttempts = config.patchAttempts
    override val plantAttempts = config.plantAttempts
    override val validBiomeTypes = config.validBiomeTypes.toMutableList()
    override val inverted = config.inverted

    override fun generatePlants(world: World, rand: Random, targetPos: BlockPos) {
        for (i in 0..plantAttempts) {
            val pos = targetPos.add(
                rand.nextInt(8) - rand.nextInt(8),
                rand.nextInt(4) - rand.nextInt(4),
                rand.nextInt(8) - rand.nextInt(8)
            )

            if (!world.isBlockLoaded(pos)) continue

            if (FluidUtil.canGenerateInFluids(block.compatibleFluids, world, pos)) {
                placePlant(world, pos, rand)
            }
        }
    }

    override fun placePlant(world: World, pos: BlockPos, rand: Random) {
        val state = block.defaultState

        if (block.canBlockStay(world, pos, state)) {
            world.setBlockState(pos, state, Constants.BlockFlags.SEND_TO_CLIENTS)

            if (rand.nextDouble() < 0.05) {
                if (block.canBlockStay(world, pos.up(), state)) {
                    world.setBlockState(pos.up(), state, Constants.BlockFlags.SEND_TO_CLIENTS)
                }
            }
        }
    }
}
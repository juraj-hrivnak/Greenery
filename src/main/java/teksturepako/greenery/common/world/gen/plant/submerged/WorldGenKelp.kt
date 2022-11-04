package teksturepako.greenery.common.world.gen.plant.submerged

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.util.Constants
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.world.gen.AbstractPlantGenerator
import java.util.*


class WorldGenKelp : AbstractPlantGenerator()
{
    override val block = ModBlocks.blockKelp
    private val config = Config.plant.submerged.kelp

    override val generationChance = config.generationChance
    override val patchAttempts = config.patchAttempts
    override val plantAttempts = config.plantAttempts
    override val validBiomeTypes = config.validBiomeTypes.toMutableList()
    override val inverted = config.inverted

    override fun generatePlants(world: World, rand: Random, targetPos: BlockPos)
    {
        for (i in 0..plantAttempts)
        {
            val pos = targetPos.add(
                rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8)
            )

            if (!world.isBlockLoaded(pos)) continue

            if (block.canGenerateBlockAt(world, pos))
            {
                placePlant(world, pos, rand)
            }
        }
    }

    override fun placePlant(world: World, pos: BlockPos, rand: Random)
    {
        val startingAge = rand.nextInt(block.getMaxAge() / 2)
        val height = block.getMaxAge() - startingAge

        for (i in 0..height)
        {
            val kelpPos = pos.up(i)
            val state = block.defaultState.withProperty(block.getAgeProperty(), i + startingAge)

            if (block.canGenerateBlockAt(world, kelpPos))
            {
                world.setBlockState(kelpPos, state, Constants.BlockFlags.SEND_TO_CLIENTS)
            }
            else break
        }
    }
}
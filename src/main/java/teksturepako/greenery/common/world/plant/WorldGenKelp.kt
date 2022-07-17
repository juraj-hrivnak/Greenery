package teksturepako.greenery.common.world.plant

import net.minecraft.block.material.Material
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.world.GreeneryWorldGenerator
import java.util.*


class WorldGenKelp : GreeneryWorldGenerator() {

    override val block = ModBlocks.blockKelp
    private val config = Config.generation.kelp

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

            val blockState = world.getBlockState(pos)
            if (block.compatibleFluids.isNotEmpty()) {
                val blockName = blockState.block.registryName?.namespace
                if (blockName in block.compatibleFluids) {
                    placePlant(world, pos, rand)
                }
            } else if (blockState.material == Material.WATER) {
                placePlant(world, pos, rand)
            }
        }
    }

    override fun placePlant(world: World, pos: BlockPos, rand: Random) {
        val startingAge = rand.nextInt(block.getMaxAge() / 2)
        val height = block.getMaxAge() - startingAge

        for (i in 0 until height) {
            val kelpPos = pos.up(i)
            val state = block.defaultState.withProperty(block.getAgeProperty(), i + startingAge)

            if (block.canBlockStay(world, kelpPos, state)) {
                world.setBlockState(kelpPos, state, 2)
            } else break
        }
    }
}
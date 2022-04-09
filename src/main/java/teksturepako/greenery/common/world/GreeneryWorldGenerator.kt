package teksturepako.greenery.common.world

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.fml.common.IWorldGenerator
import teksturepako.greenery.common.block.AbstractGreeneryCropBase
import teksturepako.greenery.common.util.WorldGenUtil
import teksturepako.greenery.common.util.WorldGenUtil.areBiomeTypesValid
import java.util.*

abstract class GreeneryWorldGenerator : IWorldGenerator {

    abstract val block: AbstractGreeneryCropBase

    abstract val generationChance: Double
    abstract val patchAttempts: Int
    abstract val plantAttempts: Int
    abstract val validBiomeTypes: Array<String>

    override fun generate(
        rand: Random,
        chunkX: Int,
        chunkZ: Int,
        world: World,
        chunkGenerator: IChunkGenerator,
        chunkProvider: IChunkProvider
    ) {
        val random = world.rand
        val chunkPos = world.getChunk(chunkX, chunkZ).pos
        val biome = WorldGenUtil.getBiomeInChunk(world, chunkX, chunkZ)

        if (rand.nextDouble() < generationChance && areBiomeTypesValid(biome, validBiomeTypes)) {
            for (i in 0..patchAttempts) {
                val x = random.nextInt(16) + 8
                val z = random.nextInt(16) + 8

                val yRange = world.getHeight(chunkPos.getBlock(0, 0, 0).add(x, 0, z)).y + 32
                val y = random.nextInt(yRange)

                val pos = chunkPos.getBlock(0, 0, 0).add(x, y, z)
                generatePlants(world, random, pos)
            }
        }
    }

    private fun generatePlants(world: World, rand: Random, targetPos: BlockPos) {
        for (i in 0..plantAttempts) {
            val pos = targetPos.add(
                rand.nextInt(8) - rand.nextInt(8),
                rand.nextInt(4) - rand.nextInt(4),
                rand.nextInt(8) - rand.nextInt(8)
            )

            placePlant(world, pos, rand)
        }
    }

    private fun placePlant(world: World, pos: BlockPos, rand: Random) {
        val startingAge = rand.nextInt(block.maxAge)
        val state = block.defaultState.withProperty(block.ageProperty, startingAge)

        if (block.canBlockStay(world, pos, state)) {
            world.setBlockState(pos, state, 2)
        }
    }

}
package teksturepako.greenery.common.world.gen

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import teksturepako.greenery.common.util.WorldGenUtil
import teksturepako.greenery.common.util.WorldGenUtil.areBiomeTypesValid
import java.util.*

abstract class AbstractPlantGenerator : IPlantGenerator
{
    override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkGenerator, chunkProvider: IChunkProvider)
    {
        val random = world.rand
        val chunkPos = world.getChunk(chunkX, chunkZ).pos
        val biome = WorldGenUtil.getBiomeInChunk(world, chunkX, chunkZ)

        if (rand.nextDouble() < generationChance && areBiomeTypesValid(biome, validBiomeTypes, inverted))
        {
            for (i in 0..patchAttempts)
            {
                val x = random.nextInt(16) + 8
                val z = random.nextInt(16) + 8

                val yRange = world.getHeight(chunkPos.getBlock(0, 0, 0).add(x, 0, z)).y + 32
                val y = random.nextInt(yRange)

                val pos = chunkPos.getBlock(0, 0, 0).add(x, y, z)
                generatePlants(world, random, pos)
            }
        }
    }

    override fun generatePlants(world: World, rand: Random, targetPos: BlockPos)
    {
        for (i in 0..plantAttempts)
        {
            val pos = targetPos.add(
                rand.nextInt(8) - rand.nextInt(8),
                rand.nextInt(4) - rand.nextInt(4),
                rand.nextInt(8) - rand.nextInt(8)
            )

            if (!world.isBlockLoaded(pos)) continue

            if (world.isAirBlock(pos))
            {
                placePlant(world, pos, rand)
            }
        }
    }

    abstract fun placePlant(world: World, pos: BlockPos, rand: Random)
}
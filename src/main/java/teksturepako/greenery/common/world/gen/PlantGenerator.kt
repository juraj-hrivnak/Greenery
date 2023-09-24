package teksturepako.greenery.common.world.gen

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import teksturepako.greenery.common.block.plant.GreeneryPlant
import teksturepako.greenery.common.block.plant.upland.tall.TallPlantBase
import teksturepako.greenery.common.util.WorldGenUtil
import teksturepako.greenery.common.world.WorldGenParser
import java.util.*

class PlantGenerator(override val block: GreeneryPlant) : IPlantGenerator
{
    override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkGenerator, chunkProvider: IChunkProvider)
    {
        val random = world.rand
        val chunkPos = world.getChunk(chunkX, chunkZ).pos
        val biome = WorldGenUtil.getBiomeInChunk(world, chunkX, chunkZ)
        val dimension = world.provider.dimension
        val generationModifier = if (block is TallPlantBase) 4 else 1

        /* Gets worldGen configuration from the block. */
        for (input in block.worldGen)
        {
            /* New instance of the worldGen parser class. */
            val config = WorldGenParser(
                indexedInput = input,
                worldGenConfig = block.worldGen
            )

            /* Check if plant can generate. */
            if (config.canGenerate(biome, dimension) && random.nextDouble() < config.getGenerationChance())
            {
                /* Generate patches of the plant. */
                for (i in 0..config.getPatchAttempts() * generationModifier / (block.worldGen.size - 1).coerceAtLeast(1))
                {
                    val x = random.nextInt(16) + 8
                    val z = random.nextInt(16) + 8

                    val yRange = world.getHeight(chunkPos.getBlock(0, 0, 0).add(x, 0, z)).y + 32
                    val y = random.nextInt(yRange)

                    val pos = chunkPos.getBlock(0, 0, 0).add(x, y, z)
                    generatePlants(config.getPlantAttempts(), world, random, pos, 2)
                }
            }
        }
    }

    override fun generatePlants(plantAttempts: Int, world: World, rand: Random, targetPos: BlockPos, flags: Int)
    {
        for (i in 0..plantAttempts)
        {
            val pos = targetPos.add(
                rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8)
            )

            if (!world.isBlockLoaded(pos)) continue

            block.placePlant(world, pos, rand, flags)
        }
    }
}
package teksturepako.greenery.common.world.gen.plant.upland

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import teksturepako.greenery.common.block.plant.upland.tall.AbstractTallPlant
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.util.WorldGenUtil
import teksturepako.greenery.common.util.WorldGenUtil.areBiomeTypesValid
import teksturepako.greenery.common.world.gen.AbstractPlantGenerator
import java.util.*

abstract class AbstractTallPlantGenerator : AbstractPlantGenerator()
{
    abstract override val block: AbstractTallPlant

    override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkGenerator, chunkProvider: IChunkProvider)
    {
        val random = world.rand
        val chunkPos = world.getChunk(chunkX, chunkZ).pos
        val biome = WorldGenUtil.getBiomeInChunk(world, chunkX, chunkZ)

        if (rand.nextDouble() < generationChance && areBiomeTypesValid(biome, validBiomeTypes, inverted))
        {
            for (i in 0..patchAttempts * Config.global.generationMultiplier)
            {
                val x = random.nextInt(16) + 8
                val z = random.nextInt(16) + 8

                val yRange = world.getHeight(chunkPos.getBlock(0, 0, 0).add(x, 0, z)).y + 32
                val y = random.nextInt(yRange)

                val pos = chunkPos.getBlock(0, 0, 0).add(x, y, z)
                generatePlants(world, random, pos, 2)
            }
        }
    }

    override fun placePlant(world: World, pos: BlockPos, rand: Random, flags: Int)
    {
        val startingAge = rand.nextInt(block.maxAge)
        val state = block.defaultState.withProperty(block.ageProperty, startingAge)
        val maxState = block.defaultState.withProperty(block.ageProperty, block.maxAge)

        if (block.canBlockStay(world, pos, state))
        {
            world.setBlockState(pos, state, flags)

            if (rand.nextDouble() < 0.2)
            {
                world.setBlockState(pos, maxState, flags)

                if (world.isAirBlock(pos.up()) && block.canBlockStay(world, pos.up(), state))
                {
                    world.setBlockState(pos.up(), state, flags)
                }
            }
        }
    }
}


package teksturepako.greenery.common.worldGen

import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.util.MaterialUtil
import teksturepako.greenery.common.util.WorldGenUtil
import java.util.*

class ArbBlockGenerator(
    override val name: String,
    override val blockStates: List<IBlockState>,
    override val worldGen: List<String>,
    override val allowedSoils: List<String>,
) : IArbBlockGenerator
{
    override fun generate(
        rand: Random,
        chunkX: Int,
        chunkZ: Int,
        world: World,
        chunkGenerator: IChunkGenerator,
        chunkProvider: IChunkProvider
    )
    {
        val random = world.rand
        val chunkPos = world.getChunk(chunkX, chunkZ).pos
        val biome = WorldGenUtil.getBiomeInChunk(world, chunkX, chunkZ)
        val dimension = world.provider.dimension

        // Handle super-flat worlds
        if (!Config.global.genInSuperflat && world.worldType == WorldType.FLAT) return

        // Gets worldGen configuration from the block
        for (input in worldGen)
        {
            // New instance of the worldGen parser class
            val parser = WorldGenParser(currentConfig = input, allConfigs = worldGen)

            // Check if plants can generate
            if (random.nextDouble() >= parser.generationChance || !parser.canGenerate(biome, dimension)) continue

            // Generate patches of the plant
            for (i in 0..parser.patchAttempts())
            {
                val x = random.nextInt(16) + 8
                val z = random.nextInt(16) + 8

                val yRange = world.getHeight(chunkPos.getBlock(0, 0, 0).add(x, 0, z)).y + 32
                val y = random.nextInt(yRange)

                val pos = chunkPos.getBlock(0, 0, 0).add(x, y, z)
                generateBlocks(parser.plantAttempts, world, random, pos, 2)
            }
        }
    }

    override fun generateBlocks(plantAttempts: Int, world: World, rand: Random, targetPos: BlockPos, flags: Int)
    {
        for (i in 0..plantAttempts)
        {
            val pos = targetPos.add(
                rand.nextInt(8) - rand.nextInt(8),
                rand.nextInt(4) - rand.nextInt(4),
                rand.nextInt(8) - rand.nextInt(8)
            )

            if (!world.isBlockLoaded(pos)) continue

            placeBlocks(world, pos, flags)
        }
    }

    private fun placeBlocks(world: World, pos: BlockPos, flags: Int)
    {
        val down = world.getBlockState(pos.down())
        val materials = MaterialUtil.materialsOf(allowedSoils)

        val here = world.getBlockState(pos)

        if (down.material !in materials) return
        if (blockStates.firstOrNull()?.block?.canPlaceBlockAt(world, pos) != true) return
        if (here.material == Material.LAVA || here.material == Material.WATER) return

        for ((i, blockState) in blockStates.withIndex())
        {
            val incrementedPose = pos.up(i)

            if (blockState.block.canPlaceBlockAt(world, incrementedPose))
            {
                world.setBlockState(incrementedPose, blockState, flags)
            }
            else break
        }
    }
}
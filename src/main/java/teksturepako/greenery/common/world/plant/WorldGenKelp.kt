package teksturepako.greenery.common.world.plant

import com.charles445.simpledifficulty.api.SDFluids
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.fml.common.IWorldGenerator
import teksturepako.greenery.ModConfig
import teksturepako.greenery.common.block.plant.saltwater.BlockKelp
import teksturepako.greenery.common.block.plant.saltwater.BlockKelp.Companion.AGE
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.util.WorldGenUtil
import java.util.*


class WorldGenKelp : IWorldGenerator {
    override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkGenerator, chunkProvider: IChunkProvider) {
        val biome = WorldGenUtil.getBiomeInChunk(world, chunkX, chunkZ)
        if (ModBlocks.blockKelp.isBiomeValid(biome) && world.worldType != WorldType.FLAT) {
            if (rand.nextFloat() < ModConfig.Kelp.generationChance) {
                val chunkPos = world.getChunk(chunkX, chunkZ).pos

                for (i in 0 until ModConfig.Kelp.patchAttempts * 2) {
                    val x = rand.nextInt(16) + 8
                    val z = rand.nextInt(16) + 8

                    val yRange = world.getHeight(chunkPos.getBlock(0, 0, 0).add(x, 0, z)).y + 32
                    val y = rand.nextInt(yRange)

                    val pos = chunkPos.getBlock(0, 0, 0).add(x, y, z)
                    generateKelp(world, rand, pos)
                }
            }
        }
    }

    private fun generateKelp(world: World, rand: Random, targetPos: BlockPos) {
        if (!ModConfig.Kelp.canGenerate(world, targetPos) || (!ModConfig.Kelp.generatesUnderground && !WorldGenUtil.canSeeSky(world, targetPos))) {
            return
        }

        for (i in 0 until ModConfig.Kelp.plantAttempts) {
            val pos = targetPos.add(
                rand.nextInt(10) - rand.nextInt(10),
                rand.nextInt(4) - rand.nextInt(4),
                rand.nextInt(10) - rand.nextInt(10)
            )

            if (!world.isBlockLoaded(pos)) continue

            if (world.getBlockState(pos).block == SDFluids.blockSaltWater && pos.y < 64) {
                placeKelp(world, pos, rand)
            }
        }
    }

    private fun placeKelp(world: World, pos: BlockPos, rand: Random) {
        val startingAge = rand.nextInt(BlockKelp.MAX_AGE / 2)
        val height = BlockKelp.MAX_AGE - startingAge

        for (i in 0 until height) {
            val kelpPos = pos.up(i)
            val state = ModBlocks.blockKelp.defaultState.withProperty(AGE, i + startingAge)

            if (ModBlocks.blockKelp.canBlockStay(world, kelpPos, state)) {
                world.setBlockState(kelpPos, state, 2)
            } else break
        }
    }
}
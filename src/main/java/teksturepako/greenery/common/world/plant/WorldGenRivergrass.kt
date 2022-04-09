package teksturepako.greenery.common.world.plant

import com.charles445.simpledifficulty.api.SDFluids
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.fml.common.IWorldGenerator
import teksturepako.greenery.ModConfig
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.util.WorldGenUtil
import java.util.*

class WorldGenRivergrass : IWorldGenerator {
    override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkGenerator, chunkProvider: IChunkProvider) {
        if (world.worldType != WorldType.FLAT) {
            if (rand.nextFloat() < ModConfig.Seagrass.generationChance) {
                val chunkPos = world.getChunk(chunkX, chunkZ).pos

                for (i in 0..ModConfig.Seagrass.patchAttempts * 14) {
                    val x = rand.nextInt(16) + 8
                    val z = rand.nextInt(16) + 8

                    val yRange = world.getHeight(chunkPos.getBlock(0, 0, 0).add(x, 0, z)).y + 32
                    val y = rand.nextInt(yRange)

                    val pos = chunkPos.getBlock(0, 0, 0).add(x, y, z)
                    generateRivergrass(world, rand, pos)
                }
            }
        }
    }

    private fun generateRivergrass(world: World, rand: Random, targetPos: BlockPos) {
        if (!ModConfig.Seagrass.canGenerate(world, targetPos)) {
            return
        }

        for (i in 0..ModConfig.Seagrass.plantAttempts) {
            val pos = targetPos.add(
                    rand.nextInt(8) - rand.nextInt(8),
                    rand.nextInt(4) - rand.nextInt(4),
                    rand.nextInt(8) - rand.nextInt(8)
            )

            if (!world.isBlockLoaded(pos)) continue

            val block = world.getBlockState(pos).block
            if ((block == SDFluids.blockPurifiedWater || block == Blocks.WATER) && pos.y < 64 && pos.y > 44 && !WorldGenUtil.canSeeSky(world, targetPos)) {
                placeRivergrass(world, pos, rand)
            }
        }
    }

    private fun placeRivergrass(world: World, pos: BlockPos, rand: Random) {
        val state = ModBlocks.blockRivergrass.defaultState

        if (ModBlocks.blockRivergrass.canBlockStay(world, pos, state)) {
            world.setBlockState(pos, state, 2)

            if (rand.nextDouble() < 0.05) {
                if (ModBlocks.blockRivergrass.canBlockStay(world, pos.up(), state)) {
                    world.setBlockState(pos.up(), state, 2)
                }
            }
        }
    }
}
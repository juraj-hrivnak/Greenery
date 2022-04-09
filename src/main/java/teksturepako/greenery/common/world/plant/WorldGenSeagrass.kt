package teksturepako.greenery.common.world.plant

import com.charles445.simpledifficulty.api.SDFluids
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

class WorldGenSeagrass : IWorldGenerator {
    override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkGenerator, chunkProvider: IChunkProvider) {
        if (world.worldType != WorldType.FLAT) {
            if (rand.nextFloat() < ModConfig.Seagrass.generationChance * 8) {
                val chunkPos = world.getChunk(chunkX, chunkZ).pos

                for (i in 0..ModConfig.Seagrass.patchAttempts) {
                    val x = rand.nextInt(16) + 8
                    val z = rand.nextInt(16) + 8

                    val yRange = world.getHeight(chunkPos.getBlock(0, 0, 0).add(x, 0, z)).y + 32
                    val y = rand.nextInt(yRange)

                    val pos = chunkPos.getBlock(0, 0, 0).add(x, y, z)
                    generateSeagrass(world, rand, pos)
                }
            }
        }
    }

    private fun generateSeagrass(world: World, rand: Random, targetPos: BlockPos) {
        if (!ModConfig.Seagrass.canGenerate(world, targetPos) || (!ModConfig.Seagrass.generatesUnderground && !WorldGenUtil.canSeeSky(world, targetPos))) {
            return
        }

        for (i in 0..ModConfig.Seagrass.plantAttempts) {
            val pos = targetPos.add(
                    rand.nextInt(8) - rand.nextInt(8),
                    rand.nextInt(4) - rand.nextInt(4),
                    rand.nextInt(8) - rand.nextInt(8)
            )

            if (!world.isBlockLoaded(pos)) continue

            if (world.getBlockState(pos).block == SDFluids.blockSaltWater && pos.y < 64) {
                placeSeagrass(world, pos, rand)
            }
        }
    }

    private fun placeSeagrass(world: World, pos: BlockPos, rand: Random) {
        val state = ModBlocks.blockSeagrass.defaultState

        if (ModBlocks.blockSeagrass.canBlockStay(world, pos, state)) {
            world.setBlockState(pos, state, 2)

            //50% to generate it 2 block high (if possible)
            if (rand.nextDouble() < 0.5) {
                if (ModBlocks.blockSeagrass.canBlockStay(world, pos.up(), state)) {
                    world.setBlockState(pos.up(), state, 2)
                }
            }
        }
    }
}
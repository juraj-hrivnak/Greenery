package teksturepako.greenery.common.world.plant

import net.minecraft.block.material.Material
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.util.WorldGenUtil
import teksturepako.greenery.common.world.IGreeneryWorldGenerator
import java.util.*


class WorldGenKelp : IGreeneryWorldGenerator {

    override val block = ModBlocks.blockKelp
    private val config = Config.generation.kelp

    override val generationChance = config.generationChance
    override val patchAttempts = config.patchAttempts
    override val plantAttempts = config.plantAttempts
    override val validBiomeTypes = config.validBiomeTypes.toMutableList()
    override val inverted = config.inverted

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

        if (rand.nextDouble() < generationChance && WorldGenUtil.areBiomeTypesValid(biome, validBiomeTypes, inverted)) {
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

    override fun generatePlants(world: World, rand: Random, targetPos: BlockPos) {
        for (i in 0..plantAttempts) {
            val pos = targetPos.add(
                rand.nextInt(10) - rand.nextInt(10),
                rand.nextInt(4) - rand.nextInt(4),
                rand.nextInt(10) - rand.nextInt(10)
            )

            if (!world.isBlockLoaded(pos)) continue

            val blockState = world.getBlockState(pos)

            if (blockState.material == Material.WATER && pos.y < 64 && pos.y > 44 && !WorldGenUtil.canSeeSky(
                    world,
                    targetPos
                )
            ) {
                placeKelp(world, pos, rand)
            }
        }
    }

    private fun placeKelp(world: World, pos: BlockPos, rand: Random) {
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
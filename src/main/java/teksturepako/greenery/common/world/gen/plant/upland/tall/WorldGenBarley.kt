package teksturepako.greenery.common.world.gen.plant.upland.tall

import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraft.world.biome.Biome.REGISTRY
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.util.WorldGenUtil
import teksturepako.greenery.common.util.WorldGenUtil.areBiomeTypesValid
import teksturepako.greenery.common.world.gen.plant.upland.AbstractTallPlantGenerator
import java.util.*

class WorldGenBarley : AbstractTallPlantGenerator()
{
    override val block = ModBlocks.blockBarley
    private val config = Config.plant.upland.tall.barley

    override val generationChance = config.generationChance
    override val patchAttempts = config.patchAttempts
    override val plantAttempts = config.plantAttempts
    override val validBiomeTypes = config.validBiomeTypes.toMutableList()
    override val inverted = config.inverted

    override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkGenerator, chunkProvider: IChunkProvider)
    {
        val random = world.rand
        val chunkPos = world.getChunk(chunkX, chunkZ).pos
        val biome = WorldGenUtil.getBiomeInChunk(world, chunkX, chunkZ)

        if ((rand.nextDouble() < generationChance && areBiomeTypesValid(
                biome, validBiomeTypes, inverted
            )) && biome != REGISTRY.getObject(ResourceLocation("biomesoplenty", "pasture")))
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
        else if (biome == REGISTRY.getObject(ResourceLocation("biomesoplenty", "pasture")))
        {
            for (i in 0..64 * 20)
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
}


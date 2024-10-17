package teksturepako.greenery.common.worldGen

import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.fml.common.IWorldGenerator
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.util.WorldGenUtil.removeUnwantedBopGenerators
import java.util.*

internal class WorldGenHook : IWorldGenerator
{
    override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGen: IChunkGenerator, chunkProv: IChunkProvider)
    {
        val plantGenerators: MutableList<IPlantGenerator> = Greenery.loadPlantGenerators(true)
        for (generator in plantGenerators) generator.generate(rand, chunkX, chunkZ, world, chunkGen, chunkProv)

        val arbBlockGenerators: MutableList<IArbBlockGenerator> = Greenery.loadArbBlockGenerators(true)
        for (generator in arbBlockGenerators) generator.generate(rand, chunkX, chunkZ, world, chunkGen, chunkProv)

        if (Config.global.removeGrass) removeUnwantedBopGenerators(world)
    }
}
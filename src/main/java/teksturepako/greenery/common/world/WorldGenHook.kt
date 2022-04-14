package teksturepako.greenery.common.world

import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.fml.common.IWorldGenerator
import teksturepako.greenery.Greenery
import teksturepako.greenery.api.world.IGreeneryWorldGenerator
import java.util.*

class WorldGenHook : IWorldGenerator {

    override fun generate(
        rand: Random,
        chunkX: Int,
        chunkZ: Int,
        world: World,
        chunkGen: IChunkGenerator,
        chunkProv: IChunkProvider
    ) {
        val generators: MutableList<IGreeneryWorldGenerator> = Greenery.loadGenerators()
        for (generator in generators) {
            generator.generate(rand, chunkX, chunkZ, world, chunkGen, chunkProv)
        }
    }
}
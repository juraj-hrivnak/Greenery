package teksturepako.greenery.common.world.flowers

import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import teksturepako.greenery.common.block.flower.AbstractFlower
import java.util.*

class WorldGenNetherFlowers(flower: AbstractFlower) : WorldGenCustomFlowers(flower) {
    override fun getGenerationPos(world: World, rand: Random, chunkPos: ChunkPos): BlockPos {
        val x = rand.nextInt(16) + 8
        val z = rand.nextInt(16) + 8
        val y = rand.nextInt(64) + 32

        return chunkPos.getBlock(0, 0, 0).add(x, y, z)
    }

    override fun canGenerateInWorld(world: World): Boolean {
        return world.provider.dimension == -1
    }

    override fun canGenerateOnBlock(world: World, pos: BlockPos): Boolean {
        return world.getBlockState(pos.down()).block == Blocks.SOUL_SAND
    }
}
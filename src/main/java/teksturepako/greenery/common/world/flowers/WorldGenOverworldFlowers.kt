package teksturepako.greenery.common.world.flowers

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.WorldType
import teksturepako.greenery.common.block.flower.AbstractFlower
import java.util.*

class WorldGenOverworldFlowers(flower: AbstractFlower) :  WorldGenCustomFlowers(flower) {
    override fun getGenerationPos(world: World, rand: Random, chunkPos: ChunkPos): BlockPos {
        val x = rand.nextInt(16) + 8
        val z = rand.nextInt(16) + 8

        val yRange = world.getHeight(chunkPos.getBlock(0, 0, 0).add(x, 0, z)).y + 32
        val y = rand.nextInt(yRange)

        return chunkPos.getBlock(0, 0, 0).add(x, y, z)
    }

    override fun canGenerateInWorld(world: World): Boolean {
        return world.worldType != WorldType.FLAT
    }
}
package teksturepako.greenery.common.handler.event

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.util.Constants.BlockFlags.DEFAULT_AND_RERENDER
import net.minecraftforge.event.entity.player.BonemealEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.util.WorldGenUtil
import java.util.*

@Mod.EventBusSubscriber
object EventBonemeal
{
    private const val GENERATION_ATTEMPTS = 8

    private fun spawnParticles(worldIn: World, pos: BlockPos, rand: Random)
    {
        for (i in 0 until GENERATION_ATTEMPTS)
        {
            val blockPos = pos.add(
                rand.nextInt(4) - rand.nextInt(4), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(4) - rand.nextInt(4)
            )

            var d1 = (blockPos.x.toFloat() + rand.nextFloat()).toDouble()
            var d2 = (blockPos.y.toFloat() + rand.nextFloat()).toDouble()
            var d3 = (blockPos.z.toFloat() + rand.nextFloat()).toDouble()
            when
            {
                i == 0 && !worldIn.getBlockState(blockPos.up()).isOpaqueCube    -> d2 = blockPos.y.toDouble() + 0.0625 + 1.0
                i == 1 && !worldIn.getBlockState(blockPos.down()).isOpaqueCube  -> d2 = blockPos.y.toDouble() - 0.0625
                i == 2 && !worldIn.getBlockState(blockPos.south()).isOpaqueCube -> d3 = blockPos.z.toDouble() + 0.0625 + 1.0
                i == 3 && !worldIn.getBlockState(blockPos.north()).isOpaqueCube -> d3 = blockPos.z.toDouble() - 0.0625
                i == 4 && !worldIn.getBlockState(blockPos.east()).isOpaqueCube  -> d1 = blockPos.x.toDouble() + 0.0625 + 1.0
                i == 5 && !worldIn.getBlockState(blockPos.west()).isOpaqueCube  -> d1 = blockPos.x.toDouble() - 0.0625
            }
            if (d1 < blockPos.x.toDouble() || d1 > (blockPos.x + 1).toDouble() || d2 < 0.0 || d2 > (blockPos.y + 1).toDouble() || d3 < blockPos.z.toDouble() || d3 > (blockPos.z + 1).toDouble())
            {
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d1, d2, d3, 0.0, 0.0, 0.0)
            }
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun onBonemealUsed(event: BonemealEvent)
    {
        if (!Config.global.genPlantsFromBonemeal) return

        val world = event.world
        val block = event.block
        val up = event.pos.up()
        val rand = world.rand

        if (block.material == Material.GRASS || block.block.registryName == ForgeRegistries.BLOCKS.getValue(
                ResourceLocation("biomesoplenty:grass")
            )?.registryName && block.isFullBlock)
        {
            event.stack.count -= 1
            event.isCanceled = true
        }

        if (Greenery.generators.isNotEmpty())
        {
            for (generator in Greenery.generators)
            {
                if (WorldGenUtil.areBiomeTypesValid(
                        world.getBiome(up), generator.validBiomeTypes, generator.inverted
                    ) && generator.block.canPlaceBlockAt(world, up) && block != generator.block)
                {
                    if (!world.isRemote)
                    {
                        event.result = Event.Result.ALLOW
                        grow(generator.block, up, world, rand)
                    }
                    else if (event.entityPlayer == Minecraft.getMinecraft().player)
                    {
                        Minecraft.getMinecraft().player.swingArm(event.hand!!)
                        spawnParticles(world, up, rand)
                    }
                }
            }
        }
    }

    private fun grow(block: Block, pos: BlockPos, world: World, rand: Random)
    {
        for (i in 0 until GENERATION_ATTEMPTS)
        {
            val blockPos = pos.add(
                rand.nextInt(4) - rand.nextInt(4), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(4) - rand.nextInt(4)
            )

            if (block.canPlaceBlockAt(world, blockPos))
            {
                world.notifyBlockUpdate(blockPos, world.getBlockState(blockPos), block.defaultState, DEFAULT_AND_RERENDER)
                world.setBlockState(blockPos, block.defaultState)
            }
        }
    }
}
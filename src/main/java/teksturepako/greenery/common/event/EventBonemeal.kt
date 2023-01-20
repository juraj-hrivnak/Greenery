package teksturepako.greenery.common.event

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
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
    @SubscribeEvent
    @JvmStatic
    fun onBonemealUsed(event: BonemealEvent)
    {
        if (!Config.global.genPlantsFromBonemeal) return

        val world = event.world
        val blockState = event.block
        val up = event.pos.up()
        val rand = world.rand

        when
        {
            blockState.block.registryName == ForgeRegistries.BLOCKS.getValue(
                ResourceLocation("biomesoplenty:grass")
            )?.registryName && blockState.isFullBlock                                       ->
            {
                event.stack.count -= 1
                event.isCanceled = true

                useBonemeal(event, blockState, up, world, rand)
            }
            blockState.material == Material.GRASS                                           ->
            {
                useBonemeal(event, blockState, up, world, rand)
            }
            FluidloggedUtils.isFluidloggableFluid(world.getBlockState(up).block)            ->
            {
                useBonemeal(event, blockState, up, world, rand)
            }
        }
    }

    private fun useBonemeal(event: BonemealEvent, block: IBlockState, pos: BlockPos, world: World, rand: Random)
    {
        if (Greenery.generators.isNotEmpty())
        {
            for (generator in Greenery.generators)
            {
                if (WorldGenUtil.areBiomeTypesValid(
                        world.getBiome(pos), generator.validBiomeTypes, generator.inverted
                    ) && generator.block.canPlaceBlockAt(world, pos) && block != generator.block)
                {
                    val generationAttempts = (generator.patchAttempts / generator.generationChance).toInt()
                    if (!world.isRemote)
                    {
                        event.result = Event.Result.ALLOW
                        generator.generatePlants(world, rand, pos)
                    }
                    else if (event.entityPlayer == Minecraft.getMinecraft().player)
                    {
                        Minecraft.getMinecraft().player.swingArm(event.hand!!)
                        spawnParticles(generationAttempts, world, pos, rand)
                    }
                }
            }
        }
    }

    private fun spawnParticles(generationAttempts: Int, worldIn: World, pos: BlockPos, rand: Random)
    {
        for (i in 0 until generationAttempts)
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
}
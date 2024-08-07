@file:Suppress("OVERRIDE_DEPRECATION")

package teksturepako.greenery.common.block.plant.upland.tall

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import teksturepako.greenery.Greenery
import teksturepako.greenery.Greenery.logger
import teksturepako.greenery.common.block.plant.GreeneryPlant
import teksturepako.greenery.common.block.plantContainer
import teksturepako.greenery.common.util.MaterialUtil
import teksturepako.greenery.common.util.Utils.applyOffset
import java.util.*

abstract class TallUplandPlant(val name: String, maxAge: Int) : GreeneryPlant(maxAge)
{
    companion object
    {
        val GRASS_TOP_AABB = arrayOf(
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.50, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.625, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.75, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.875, 0.9)
        )
        val GRASS_BOTTOM_AABB = arrayOf(
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 1.0, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 1.0, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 1.0, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 1.0, 0.9)
        )
    }

    // -- BLOCK STATE --

    private val topProperty: PropertyBool = PropertyBool.create("top")
    private val singleProperty: PropertyBool = PropertyBool.create("single")

    override fun createPlantContainer() = plantContainer(ageProperty, topProperty, singleProperty)

    init
    {
        initBlockState()
        defaultState = blockState.baseState
            .withProperty(ageProperty, 0)
            .withProperty(topProperty, true)
            .withProperty(singleProperty, false)
    }

    // -- BLOCK --

    init
    {
        setRegistryName("plant/upland/tall/$name")
        translationKey = "${Greenery.MODID}.$name"
        soundType = SoundType.PLANT
        creativeTab = Greenery.creativeTab
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState
    {
        if (worldIn.isAirBlock(pos.down())) return state // Keep the same state when breaking the block.

        val hasTheSameBlockBelow = worldIn.getBlockState(pos.down()).block == this
        val hasTheSameBlockAbove = worldIn.getBlockState(pos.up()).block == this

        return when
        {
            hasTheSameBlockBelow -> state.withProperty(topProperty, true).withProperty(singleProperty, false)
            hasTheSameBlockAbove -> state.withProperty(topProperty, false).withProperty(singleProperty, false)
            else -> state.withProperty(topProperty, true).withProperty(singleProperty, true)
        }
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return when (val actualState = getActualState(state, source, pos))
        {
            actualState.withProperty(topProperty, true)  -> GRASS_TOP_AABB[getAge(state)].applyOffset(hasOffset, state, source, pos)
            actualState.withProperty(topProperty, false) -> GRASS_BOTTOM_AABB[getAge(state)].applyOffset(hasOffset, state, source, pos)
            else -> GRASS_TOP_AABB[getAge(state)].applyOffset(hasOffset, state, source, pos)
        }
    }


    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean
    {
        val down = worldIn.getBlockState(pos.down())
        val down2 = worldIn.getBlockState(pos.down(2))

        val materials = MaterialUtil.materialsOf(allowedSoils)

        return ((down.material in materials || down.block == Blocks.DIRT)
                || (down.block == this && getAge(down) == maxAge && down2.material in materials))
    }

    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState, isClient: Boolean): Boolean
    {
        return when
        {
            worldIn.getBlockState(pos.up()).block == this -> false
            worldIn.getBlockState(pos.down()).block == this -> (getAge(state) < maxAge)
            else -> true
        }
    }

    override fun placePlant(world: World, pos: BlockPos, rand: Random, flags: Int)
    {
        if (world.isAirBlock(pos))
        {
            val startingAge = rand.nextInt(maxAge)
            val state = this.defaultState.withProperty(ageProperty, startingAge)
            val maxState = this.defaultState.withProperty(ageProperty, maxAge)

            if (this.canBlockStay(world, pos, state))
            {
                world.setBlockState(pos, state, flags)

                if (rand.nextDouble() < 0.2)
                {
                    world.setBlockState(pos, maxState, flags)

                    if (world.isAirBlock(pos.up()) && this.canBlockStay(world, pos.up(), state))
                    {
                        world.setBlockState(pos.up(), state, flags)
                    }
                }
            }
        }
    }

    override fun quantityDroppedWithBonus(fortune: Int, random: Random): Int
    {
        return 1 + random.nextInt(fortune * 2 + 1)
    }

    override fun harvestBlock(worldIn: World, player: EntityPlayer, pos: BlockPos, state: IBlockState, te: TileEntity?, stack: ItemStack)
    {
        if (!worldIn.isRemote && stack.item === Items.SHEARS)
        {
            StatList.getBlockStats(this)?.let { player.addStat(it) }
            spawnAsEntity(worldIn, pos, ItemStack(this, 1))
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack)
        }
    }
}
@file:Suppress("OVERRIDE_DEPRECATION")

package teksturepako.greenery.common.block.plant.submerged.kelplike

import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import teksturepako.greenery.common.block.plant.submerged.AbstractSubmergedPlant
import teksturepako.greenery.common.util.Utils.applyOffset
import java.util.*
import kotlin.math.min

abstract class KelpLikePlantBase(name: String, maxAge: Int) : AbstractSubmergedPlant(name, maxAge)
{
    // -- BLOCK STATE --

    private val topProperty: PropertyBool = PropertyBool.create("top")

    override fun createPlantContainer(): BlockStateContainer =
        BlockStateContainer(this, ageProperty, topProperty)

    init
    {
        initBlockState()
        defaultState = blockState.baseState
            .withProperty(ageProperty, 0)
            .withProperty(topProperty, false)
    }

    // -- BLOCK --

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState
    {
        val hasKelpAbove = worldIn.getBlockState(pos.up()).block == this
        return state.withProperty(topProperty, !hasKelpAbove)
    }

    // Block behavior
    override fun getStateForPlacement(
        world: World,
        pos: BlockPos,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float,
        meta: Int,
        placer: EntityLivingBase,
        hand: EnumHand
    ): IBlockState
    {
        val down = world.getBlockState(pos.down())
        val age = if (down.block == this)
        {
            min(down.getValue(ageProperty) + 1, maxAge)
        } else
        {
            Random().nextInt(maxAge / 2)
        }

        return defaultState.withProperty(ageProperty, age)
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean
    {
        // Must have kelp or valid soil below
        val down = worldIn.getBlockState(pos.down())
        return if (down.block == this) true else down.material in ALLOWED_SOILS
    }

    override fun placePlant(world: World, pos: BlockPos, rand: Random, flags: Int)
    {
        if (this.canGenerateBlockAt(world, pos))
        {
            val startingAge = rand.nextInt(maxAge / 2)
            val height = maxAge - startingAge

            for (i in 0..height)
            {
                val kelpPos = pos.up(i)
                val state = this.defaultState.withProperty(ageProperty, i + startingAge)

                if (this.canGenerateBlockAt(world, kelpPos))
                {
                    world.setBlockState(kelpPos, state, flags)
                } else break
            }
        }
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random)
    {
        if (worldIn.isRemote) return
        if (!worldIn.isBlockLoaded(pos.up())) return
        val age = state.getValue(ageProperty)
        if (age < maxAge && rand.nextDouble() < 0.14)
        {
            if (canGenerateBlockAt(worldIn, pos.up()))
            {
                val newBlockState = defaultState.withProperty(ageProperty, age + 1)
                if (canBlockStay(worldIn, pos.up(), state))
                {
                    worldIn.setBlockState(pos.up(), newBlockState)
                }
            }
        }
    }

    private fun getTopPosition(worldIn: World, pos: BlockPos): BlockPos
    {
        var topPos = pos
        while (worldIn.getBlockState(topPos.up()).block == this)
        {
            topPos = topPos.up()
        }

        return topPos
    }


    // IGrowable implementation
    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState, isClient: Boolean): Boolean
    {
        val topPos = getTopPosition(worldIn, pos)
        val topAge = worldIn.getBlockState(topPos).getValue(ageProperty)

        return topAge < maxAge && canGenerateBlockAt(worldIn, topPos.up())
    }

    override fun grow(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState)
    {
        val topPos = getTopPosition(worldIn, pos)
        val topAge = worldIn.getBlockState(topPos).getValue(ageProperty)

        val newBlockState = defaultState.withProperty(ageProperty, topAge + 1)
        worldIn.setBlockState(topPos.up(), newBlockState)
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return when (val actualState = getActualState(state, source, pos))
        {
            actualState.withProperty(topProperty, true)  -> TOP_AABB.applyOffset(hasOffset, state, source, pos)
            actualState.withProperty(topProperty, false) -> BOTTOM_AABB.applyOffset(hasOffset, state, source, pos)
            else                                         -> TOP_AABB.applyOffset(hasOffset, state, source, pos)
        }
    }
}
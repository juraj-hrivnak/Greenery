@file:Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")

package teksturepako.greenery.common.block.plant.submerged

import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import teksturepako.greenery.common.config.Config
import java.util.*
import kotlin.math.min

class BlockKelp : AbstractSubmergedPlant(NAME)
{
    override var worldGenConfig = Config.plant.submerged.kelp.worldGen.toMutableList()
    override var compatibleFluids = Config.plant.submerged.kelp.compatibleFluids.toMutableList()
    override var hasTintIndex = false
    override var isSolid = false
    override var isHarmful = false

    companion object
    {
        const val NAME = "kelp"

        const val MAX_AGE = 15
        val IS_TOP_BLOCK: PropertyBool = PropertyBool.create("top")
        val AGE: PropertyInteger = PropertyInteger.create("remaining_height", 0, MAX_AGE)
    }

    init
    {
        defaultState = blockState.baseState.withProperty(IS_TOP_BLOCK, false).withProperty(AGE, 0)
    }

    override fun getMaxAge(): Int
    {
        return MAX_AGE
    }

    override fun getAgeProperty(): PropertyInteger
    {
        return AGE
    }

    override fun getStateFromMeta(meta: Int): IBlockState
    {
        return defaultState.withProperty(AGE, meta)
    }

    override fun getMetaFromState(state: IBlockState): Int
    {
        return state.getValue(AGE)
    }

    override fun createBlockState(): BlockStateContainer
    {
        return BlockStateContainer(this, IS_TOP_BLOCK, AGE)
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState
    {
        val hasKelpAbove = worldIn.getBlockState(pos.up()).block == this
        return state.withProperty(IS_TOP_BLOCK, !hasKelpAbove)
    }

    //Block behavior
    override fun getStateForPlacement(world: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState
    {
        val down = world.getBlockState(pos.down())
        val age = if (down.block == this)
        {
            min(down.getValue(AGE) + 1, MAX_AGE)
        }
        else
        {
            Random().nextInt(MAX_AGE / 2)
        }

        return defaultState.withProperty(AGE, age)
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean
    {
        //Must have kelp or valid soil below
        val down = worldIn.getBlockState(pos.down())
        return if (down.block == this) true else down.material in ALLOWED_SOILS
    }

    override fun placePlant(world: World, pos: BlockPos, rand: Random, flags: Int)
    {
        if (this.canGenerateBlockAt(world, pos))
        {
            val startingAge = rand.nextInt(this.maxAge / 2)
            val height = this.maxAge - startingAge

            for (i in 0..height)
            {
                val kelpPos = pos.up(i)
                val state = this.defaultState.withProperty(this.ageProperty, i + startingAge)

                if (this.canGenerateBlockAt(world, kelpPos))
                {
                    world.setBlockState(kelpPos, state, flags)
                }
                else break
            }
        }
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random)
    {
        if (worldIn.isRemote) return
        if (!worldIn.isBlockLoaded(pos.up())) return
        val age = state.getValue(AGE)
        if (age < MAX_AGE && rand.nextDouble() < 0.14)
        {
            if (canGenerateBlockAt(worldIn, pos.up()))
            {
                val newBlockState = defaultState.withProperty(AGE, age + 1)
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
        val topAge = worldIn.getBlockState(topPos).getValue(AGE)

        return topAge < MAX_AGE && canGenerateBlockAt(worldIn, topPos.up())
    }

    override fun grow(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState)
    {
        val topPos = getTopPosition(worldIn, pos)
        val topAge = worldIn.getBlockState(topPos).getValue(AGE)

        val newBlockState = defaultState.withProperty(AGE, topAge + 1)
        worldIn.setBlockState(topPos.up(), newBlockState)
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return when (val actualState = getActualState(state, source, pos))
        {
            actualState.withProperty(IS_TOP_BLOCK, true) -> TOP_AABB.offset(state.getOffset(source, pos))
            actualState.withProperty(IS_TOP_BLOCK, false) -> BOTTOM_AABB.offset(state.getOffset(source, pos))
            else -> TOP_AABB.offset(state.getOffset(source, pos))
        }
    }
}
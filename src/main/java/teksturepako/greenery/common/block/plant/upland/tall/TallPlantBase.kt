@file:Suppress("OVERRIDE_DEPRECATION")

package teksturepako.greenery.common.block.plant.upland.tall

import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import teksturepako.greenery.common.util.Utils.applyOffset

abstract class TallPlantBase(name: String, maxAge: Int) : AbstractTallPlant(name, maxAge)
{
    // -- BLOCK STATES --

    private val topProperty: PropertyBool = PropertyBool.create("top")
    private val singleProperty: PropertyBool = PropertyBool.create("single")

    override fun createPlantContainer(): BlockStateContainer =
        BlockStateContainer(this, ageProperty, topProperty, singleProperty)

    init
    {
        initBlockState()
        defaultState = blockState.baseState
            .withProperty(ageProperty, 0)
            .withProperty(topProperty, true)
            .withProperty(singleProperty, false)
    }

    // -- BLOCK --

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
            else                                         -> GRASS_TOP_AABB[getAge(state)].applyOffset(hasOffset, state, source, pos)
        }
    }
}
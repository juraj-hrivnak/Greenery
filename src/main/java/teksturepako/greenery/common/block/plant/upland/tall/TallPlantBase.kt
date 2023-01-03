@file:Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")

package teksturepako.greenery.common.block.plant.upland.tall

import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

open class TallPlantBase(name: String, config: MutableList<String>) : AbstractTallPlant(name)
{
    companion object
    {
        val AGE: PropertyInteger = PropertyInteger.create("age", 0, 3)
        val TOP: PropertyBool = PropertyBool.create("top")
        val SINGLE: PropertyBool = PropertyBool.create("single")
    }

    init
    {
        defaultState = blockState.baseState.withProperty(AGE, 0).withProperty(TOP, true).withProperty(SINGLE, false)
    }

    override val drops: MutableList<String> = config

    override fun getAgeProperty(): PropertyInteger
    {
        return AGE
    }

    override fun getMaxAge(): Int
    {
        return 3
    }

    override fun createBlockState(): BlockStateContainer
    {
        return BlockStateContainer(this, AGE, TOP, SINGLE)
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState
    {
        val hasTheSameBlockBelow = worldIn.getBlockState(pos.down()).block == this
        val hasTheSameBlockAbove = worldIn.getBlockState(pos.up()).block == this

        return when
        {
            hasTheSameBlockBelow -> state.withProperty(TOP, true).withProperty(SINGLE, false)
            hasTheSameBlockAbove -> state.withProperty(TOP, false).withProperty(SINGLE, false)
            else                 -> state.withProperty(TOP, true).withProperty(SINGLE, true)
        }
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return when (val actualState = getActualState(state, source, pos))
        {
            actualState.withProperty(
                TOP, true
            )    -> GRASS_TOP_AABB[(state.getValue(this.ageProperty) as Int).toInt()].offset(
                state.getOffset(
                    source, pos
                )
            )
            actualState.withProperty(
                TOP, false
            )    -> GRASS_BOTTOM_AABB[(state.getValue(this.ageProperty) as Int).toInt()].offset(
                state.getOffset(source, pos)
            )
            else -> GRASS_TOP_AABB[(state.getValue(this.ageProperty) as Int).toInt()].offset(
                state.getOffset(source, pos)
            )
        }
    }
}
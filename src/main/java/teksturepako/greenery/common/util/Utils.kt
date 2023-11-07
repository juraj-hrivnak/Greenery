@file:Suppress("KotlinConstantConditions")

package teksturepako.greenery.common.util

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

object Utils
{
    fun List<String?>.isNotNull(index: Int): Boolean
    {
        return if (index in 0..lastIndex) get(index) != null && get(index)!!.isNotEmpty() else false
    }

    infix fun CharSequence.inNotNull(charSequence: CharSequence?): Boolean
    {
        return charSequence?.let { this in it } ?: false
    }

    fun AxisAlignedBB.applyOffset(hasOffset: Boolean, state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return if (hasOffset) this.offset(state.getOffset(source, pos)) else this
    }
}
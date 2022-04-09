package teksturepako.greenery.common.block

import net.minecraft.block.Block
import net.minecraft.block.BlockCrops
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

abstract class AbstractGreeneryCropBase : BlockCrops() {

    public abstract override fun getAgeProperty(): PropertyInteger
    abstract override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean

    @SideOnly(Side.CLIENT)
    override fun getOffsetType(): EnumOffsetType {
        return EnumOffsetType.XZ
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        entityIn.motionX = entityIn.motionX / 1.1
        entityIn.motionZ = entityIn.motionZ / 1.1
    }

    override fun canSustainBush(state: IBlockState): Boolean {
        return false
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 1)) return
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                val age = getAge(state)
                if (age < this.maxAge && rand.nextDouble() < 0.14) {
                    grow(worldIn, pos, state)
                }
            }
        }
    }

    override fun isPassable(worldIn: IBlockAccess, pos: BlockPos): Boolean {
        return true
    }

    @Deprecated("", ReplaceWith("false"))
    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    @Deprecated("", ReplaceWith("false"))
    override fun isOpaqueCube(state: IBlockState): Boolean {
        return false
    }

    @Deprecated("", ReplaceWith("false"))
    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        if (!canBlockStay(worldIn, pos, state)) {
            dropBlockAsItem(worldIn, pos, state, 0)
            worldIn.setBlockToAir(pos)
        }
    }

    override fun canPlaceBlockOnSide(worldIn: World, pos: BlockPos, side: EnumFacing): Boolean {
        return canBlockStay(worldIn, pos, defaultState)
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        return canBlockStay(worldIn, pos, defaultState)
    }

    // Crop
    override fun getBonemealAgeIncrease(worldIn: World): Int {
        return super.getBonemealAgeIncrease(worldIn) / this.maxAge
    }

}
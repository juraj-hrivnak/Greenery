package teksturepako.greenery.common.block

import net.minecraft.block.Block
import net.minecraft.block.BlockCrops
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teksturepako.greenery.Greenery
import java.util.*

abstract class GreeneryCropBase : BlockCrops() {

    public abstract override fun getAgeProperty(): PropertyInteger

    abstract override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean

    lateinit var itemBlock: Item

    fun createItemBlock(): Item {
        itemBlock = ItemBlock(this).setRegistryName(registryName).setTranslationKey(translationKey)
        return itemBlock
    }

    @SideOnly(Side.CLIENT)
    fun registerItemModel() {
        Greenery.proxy.registerItemBlockRenderer(itemBlock, 0, registryName.toString())
    }

    @SideOnly(Side.CLIENT)
    fun registerItemColorHandler(event: ColorHandlerEvent.Item) {
        Greenery.proxy.registerItemColourHandler(itemBlock, event)
    }

    @SideOnly(Side.CLIENT)
    fun registerBlockColorHandler(event: ColorHandlerEvent.Block) {
        Greenery.proxy.registerGrassColourHandler(this, event)
    }


    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        entityIn.motionX = entityIn.motionX / 1.1
        entityIn.motionZ = entityIn.motionZ / 1.1
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 1)) return
            if (!canBlockStay(worldIn, pos, state)) return
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                val age = getAge(state)
                if (age < this.maxAge && rand.nextDouble() < 0.14) {
                    grow(worldIn, pos, state)
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getOffsetType(): EnumOffsetType {
        return EnumOffsetType.XZ
    }

    override fun isPassable(worldIn: IBlockAccess, pos: BlockPos): Boolean {
        return true
    }

    override fun isReplaceable(worldIn: IBlockAccess, pos: BlockPos): Boolean {
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


    override fun getBonemealAgeIncrease(worldIn: World): Int {
        return super.getBonemealAgeIncrease(worldIn) / this.maxAge
    }

    override fun canSustainBush(state: IBlockState): Boolean {
        return false
    }

}
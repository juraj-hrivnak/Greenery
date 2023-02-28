@file:Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")

package teksturepako.greenery.common.block.plant

import net.minecraft.block.BlockCrops
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.util.ModDamageSource
import java.util.*

abstract class GreeneryPlantBase(override val worldGenConfig: MutableList<String>, private val isSolid: Boolean, private val doHarm: Boolean) :
        BlockCrops(), IGreeneryPlant
{
    lateinit var itemBlock: Item

    /**
     * Creates an item block
     */
    open fun createItemBlock(): Item
    {
        itemBlock = ItemBlock(this).setRegistryName(registryName).setTranslationKey(translationKey)
        return itemBlock
    }

    /**
     * Registers a model for the item block
     */
    @SideOnly(Side.CLIENT)
    fun registerItemModel()
    {
        Greenery.proxy.registerItemBlockRenderer(itemBlock, 0, registryName.toString())
    }

    /**
     * Registers a color handler for the item block
     */
    @SideOnly(Side.CLIENT)
    fun registerItemColorHandler(event: ColorHandlerEvent.Item)
    {
        Greenery.proxy.registerItemColorHandler(itemBlock, event)
    }

    /**
     * Registers a color handler for this block
     */
    @SideOnly(Side.CLIENT)
    fun registerBlockColorHandler(event: ColorHandlerEvent.Block)
    {
        Greenery.proxy.registerGrassColorHandler(this, event)
    }

    public abstract override fun getAgeProperty(): PropertyInteger

    /**
     * Determines whether the block can stay on the position, based on its surroundings.
     */
    abstract override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean

    override fun getSeed(): Item
    {
        return itemBlock
    }

    override fun getCrop(): Item
    {
        return itemBlock
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity)
    {
        entityIn.motionX = entityIn.motionX / (Config.global.slowdownModifier + 1)
        entityIn.motionZ = entityIn.motionZ / (Config.global.slowdownModifier + 1)

        if (doHarm && entityIn is EntityPlayer)
        {
            if (entityIn.inventory.armorInventory[0] == ItemStack.EMPTY)
            {
                entityIn.attackEntityFrom(ModDamageSource.NETTLE, 0.5f)
            }
            if (entityIn.inventory.armorInventory[1] == ItemStack.EMPTY)
            {
                entityIn.attackEntityFrom(ModDamageSource.NETTLE, 0.5f)
            }
        }
    }

    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB?
    {
        return if (isSolid) this.getBoundingBox(blockState, worldIn, pos) else NULL_AABB
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random)
    {
        if (getAge(state) <= this.maxAge && canBlockStay(worldIn, pos, state))
        {
            if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((25.0f / 1.0f).toInt() + 1) == 0))
            {
                grow(worldIn, pos, state)
                ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos))
            }
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getOffsetType(): EnumOffsetType
    {
        return if (Loader.isModLoaded("optifine")) EnumOffsetType.NONE else EnumOffsetType.XZ
    }

    override fun isPassable(worldIn: IBlockAccess, pos: BlockPos): Boolean
    {
        return !isSolid
    }

    override fun isReplaceable(worldIn: IBlockAccess, pos: BlockPos): Boolean
    {
        return !isSolid
    }

    override fun canPlaceBlockOnSide(worldIn: World, pos: BlockPos, side: EnumFacing): Boolean
    {
        return canBlockStay(worldIn, pos, defaultState)
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean
    {
        return canBlockStay(worldIn, pos, defaultState)
    }

    override fun getBonemealAgeIncrease(worldIn: World): Int
    {
        return super.getBonemealAgeIncrease(worldIn) / this.maxAge
    }

    override fun canSustainBush(state: IBlockState): Boolean
    {
        return false
    }

    override fun isFlammable(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Boolean
    {
        return true
    }

    override fun getFlammability(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Int
    {
        return 300
    }
}
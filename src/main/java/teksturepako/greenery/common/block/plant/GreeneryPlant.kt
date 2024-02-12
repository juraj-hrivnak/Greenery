@file:Suppress("OVERRIDE_DEPRECATION", "MemberVisibilityCanBePrivate", "DEPRECATION")

package teksturepako.greenery.common.block.plant

import net.minecraft.block.Block
import net.minecraft.block.IGrowable
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.NonNullList
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.common.EnumPlantType
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.IPlantable
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.block.plant.PlantDamageSource.Companion.Prickly
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.util.DropsUtil
import java.util.*

/**
 * Base class for creating Greenery plants.
 */
abstract class GreeneryPlant(val maxAge: Int) : Block(Material.PLANTS), IGrowable, IPlantable
{
    // -- PLANT PROPERTIES --

    /**
     * Contains world gen configuration.
     * Can be reassigned at runtime.
     */
    abstract var worldGen: MutableList<String>

    /**
     * Contains drops configuration.
     * Can be reassigned at runtime.
     */
    abstract var drops: MutableList<String>

    /**
     * Determines whether this block can grow.
     * Can be reassigned at runtime.
     */
    abstract var canGrow: Boolean

    /**
     * Determines whether this block and its itemBlock has tint index.
     * Can ***not*** be reassigned at runtime.
     */
    abstract var hasTintIndex: Boolean

    /**
     * Determines whether this block has slight random offset.
     * Can be reassigned at runtime.
     */
    abstract var hasOffset: Boolean

    /**
     * Determines whether this block is solid and replaceable.
     * Can be reassigned at runtime.
     */
    abstract var isSolid: Boolean

    /**
     * Determines whether this block is harmful (prickly).
     * Can be reassigned at runtime.
     */
    abstract var isHarmful: Boolean

    // -- BLOCK STATE --

    /** Use [createPlantContainer] instead. */
    override fun createBlockState(): BlockStateContainer = BlockStateContainer(this)

    val ageProperty: PropertyInteger = PropertyInteger.create("age", 0, maxAge)

    protected fun initBlockState()
    {
        val stateContainer: BlockStateContainer = createPlantContainer()
        this.blockState = stateContainer
        defaultState = stateContainer.baseState
    }

    abstract fun createPlantContainer(): BlockStateContainer

    open fun isMaxAge(state: IBlockState): Boolean = state.getValue(ageProperty) as Int >= maxAge

    open fun getAge(state: IBlockState): Int = state.getValue(ageProperty) as Int
    open fun withAge(age: Int): IBlockState = this.defaultState.withProperty(ageProperty, age)

    override fun getStateFromMeta(meta: Int): IBlockState = withAge(meta)
    override fun getMetaFromState(state: IBlockState): Int = getAge(state)

    // -- BLOCK --

    /**
     * Used to place the block on world generation.
     */
    abstract fun placePlant(world: World, pos: BlockPos, rand: Random, flags: Int)

    /**
     * Item Block
     */
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
    open fun registerItemModel() = Greenery.proxy.registerItemBlockRenderer(itemBlock, 0, registryName.toString())

    /**
     * Registers a color handler for the item block
     */
    @SideOnly(Side.CLIENT)
    open fun registerItemColorHandler(event: ColorHandlerEvent.Item) = Greenery.proxy.registerItemColorHandler(itemBlock, event)

    /**
     * Registers a color handler for this block
     */
    @SideOnly(Side.CLIENT)
    open fun registerBlockColorHandler(event: ColorHandlerEvent.Block) = Greenery.proxy.registerGrassColorHandler(this, event)

    /**
     * Determines whether the block can stay on its position, based on its surroundings.
     */
    abstract fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean

    open fun checkAndDropBlock(worldIn: World, pos: BlockPos, state: IBlockState)
    {
        if (!canBlockStay(worldIn, pos, state))
        {
            dropBlockAsItem(worldIn, pos, state, 0)
            worldIn.setBlockToAir(pos)
        }
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos)
        checkAndDropBlock(worldIn, pos, state)
    }

    /**
     * Custom drops handling.
     */
    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int)
    {
        drops.addAll(DropsUtil.getDrops(this.drops, world, pos, state, itemBlock, 0))
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity)
    {
        entityIn.motionX /= (Config.global.slowdownModifier * 0.1 + 1)
        entityIn.motionZ /= (Config.global.slowdownModifier * 0.1 + 1)

        if (isHarmful && entityIn is EntityPlayer)
        {
            if (entityIn.inventory.armorInventory[0] == ItemStack.EMPTY)
            {
                entityIn.attackEntityFrom(Prickly(this.localizedName), 0.5f)
            }
            if (entityIn.inventory.armorInventory[1] == ItemStack.EMPTY)
            {
                entityIn.attackEntityFrom(Prickly(this.localizedName), 0.5f)
            }
        }
    }

    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB?
    {
        return if (isSolid) getBoundingBox(blockState, worldIn, pos) else NULL_AABB
    }

    override fun isOpaqueCube(state: IBlockState): Boolean = false
    override fun isFullCube(state: IBlockState): Boolean = false

    @SideOnly(Side.CLIENT)
    override fun getRenderLayer(): BlockRenderLayer = BlockRenderLayer.CUTOUT
    override fun getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing): BlockFaceShape
    {
        return BlockFaceShape.UNDEFINED
    }

    override fun setTickRandomly(shouldTick: Boolean): Block
    {
        return if (this.canGrow) super.setTickRandomly(true) else super.setTickRandomly(shouldTick)
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random)
    {
        if (getAge(state) <= maxAge && canBlockStay(worldIn, pos, state))
        {
            if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((25.0f / 1.0f).toInt() + 1) == 0))
            {
                grow(worldIn, rand, pos, state)
                ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos))
            }
        }
    }

    fun getBonemealAgeIncrease(worldIn: World): Int = MathHelper.getInt(worldIn.rand, 2, 5) / maxAge

    @SideOnly(Side.CLIENT)
    override fun getOffsetType(): EnumOffsetType = if (hasOffset) EnumOffsetType.XZ else EnumOffsetType.NONE
    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean = canBlockStay(worldIn, pos, defaultState)
    override fun isReplaceable(worldIn: IBlockAccess, pos: BlockPos): Boolean = !isSolid
    override fun isPassable(worldIn: IBlockAccess, pos: BlockPos): Boolean = !isSolid
    override fun isFlammable(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Boolean = true
    override fun getFlammability(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Int = 300

    // -- IGrowable Implementation --

    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState, isClient: Boolean): Boolean = !isMaxAge(state)
    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState): Boolean = true
    override fun grow(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState)
    {
        var newAge = getAge(state) + getBonemealAgeIncrease(worldIn)
        if (newAge > maxAge)
        {
            newAge = maxAge
            if (worldIn.isAirBlock(pos.up()) && canBlockStay(worldIn, pos.up(), state))
            {
                worldIn.setBlockState(pos.up(), withAge(0), 2)
            }
        }
        worldIn.setBlockState(pos, withAge(newAge), 2)
    }

    // -- IPlantable Implementation --

    override fun getPlantType(world: IBlockAccess, pos: BlockPos): EnumPlantType = EnumPlantType.Plains
    override fun getPlant(world: IBlockAccess, pos: BlockPos): IBlockState = getBlockState().baseState

    init
    {
        this.setHardness(0.0f)
        this.setSoundType(SoundType.PLANT)
        this.disableStats()
        this.setLightOpacity(0)
    }

}
@file:Suppress("OVERRIDE_DEPRECATION", "MemberVisibilityCanBePrivate")

package teksturepako.greenery.common.block.plant

import net.minecraft.block.BlockCrops
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Enchantments
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.NonNullList
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.registry.ModDamageSource
import teksturepako.greenery.common.util.DropsUtil
import java.util.*


abstract class GreeneryPlant : BlockCrops()
{
    /**
     * World Generation config
     */
    abstract var worldGen: MutableList<String>

    /**
     * Drops config
     */
    abstract var drops: MutableList<String>

    /**
     * Has TintIndex?
     */
    abstract var hasTintIndex: Boolean

    /**
     * Is Solid?
     */
    abstract var isSolid: Boolean

    /**
     * Is Harmful?
     */
    abstract var isHarmful: Boolean

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

    public abstract override fun getAgeProperty(): PropertyInteger

    /**
     * Determines whether the block can stay on its position, based on its surroundings.
     */
    abstract override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean

    /**
     * The item to drop always.
     */
    override fun getSeed(): Item = itemBlock

    /**
     * The item to drop when fully grown.
     */
    override fun getCrop(): Item = itemBlock

    override fun onBlockHarvested(worldIn: World, pos: BlockPos, state: IBlockState, player: EntityPlayer)
    {
        super.onBlockHarvested(worldIn, pos, state, player)

        // Add drops
        val fortune = EnchantmentHelper.getEnchantments(player.activeItemStack)[Enchantments.FORTUNE] ?: 0
        val drops = DropsUtil.getDrops(this.drops, worldIn, pos, state, this.seed, fortune)
        drops.forEach { item ->
            spawnAsEntity(worldIn, pos, item)
        }
    }

    // Remove drops
    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int)
    {

        return
    }

    // Remove drops
    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item = Items.AIR

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity)
    {
        entityIn.motionX = entityIn.motionX / (Config.global.slowdownModifier * 0.1 + 1)
        entityIn.motionZ = entityIn.motionZ / (Config.global.slowdownModifier * 0.1 + 1)

        if (isHarmful && entityIn is EntityPlayer)
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
    override fun getOffsetType(): EnumOffsetType = EnumOffsetType.XZ

    override fun isPassable(worldIn: IBlockAccess, pos: BlockPos): Boolean = !isSolid

    override fun isReplaceable(worldIn: IBlockAccess, pos: BlockPos): Boolean = !isSolid

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean = canBlockStay(worldIn, pos, defaultState)

    override fun getBonemealAgeIncrease(worldIn: World): Int = super.getBonemealAgeIncrease(worldIn) / this.maxAge

    override fun canSustainBush(state: IBlockState): Boolean = false

    override fun isFlammable(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Boolean = true

    override fun getFlammability(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Int = 300
}
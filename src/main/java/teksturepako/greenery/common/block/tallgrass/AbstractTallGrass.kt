package teksturepako.greenery.common.block.tallgrass

import net.minecraft.block.Block
import net.minecraft.block.BlockCrops
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.tileentity.TileEntity
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
import java.util.*

abstract class AbstractTallGrass(name: String) : BlockCrops() {

    companion object {
        val ALLOWED_SOILS = setOf<Material>(
            Material.GRASS
        )
        val GRASS_TOP_AABB = arrayOf(
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 0.5, 0.899999988079071),
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 0.625, 0.899999988079071),
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 0.75, 0.899999988079071),
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 0.875, 0.899999988079071)
        )
        val GRASS_BOTTOM_AABB = arrayOf(
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 1.0, 0.899999988079071),
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 1.0, 0.899999988079071),
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 1.0, 0.899999988079071),
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 1.0, 0.899999988079071)
        )
    }

    lateinit var itemBlock: Item

    init {
        setRegistryName(name)
        translationKey = name
        soundType = SoundType.PLANT
        creativeTab = Greenery.creativeTab
    }

    fun createItemBlock(): Item {
        itemBlock = ItemBlock(this).setRegistryName(registryName).setTranslationKey(translationKey)
        return itemBlock
    }

    @SideOnly(Side.CLIENT)
    fun registerItemModel() {
        Greenery.proxy.registerItemBlockRenderer(itemBlock, 0, registryName.toString())
    }

    @SideOnly(Side.CLIENT)
    fun registerColorHandler(event: ColorHandlerEvent.Block) {
        Greenery.proxy.registerGrassColourHandler(this, event)
    }

    @SideOnly(Side.CLIENT)
    fun registerItemBlockColorHandler(event: ColorHandlerEvent.Item) {
        Greenery.proxy.registerItemColourHandler(itemBlock, event)
    }

    @SideOnly(Side.CLIENT)
    override fun getOffsetType(): EnumOffsetType {
        return EnumOffsetType.XZ
    }

    abstract override fun getAgeProperty(): PropertyInteger

    override fun getSeed(): Item {
        return itemBlock
    }

    override fun getCrop(): Item {
        return itemBlock
    }

    override fun canSustainBush(state: IBlockState): Boolean {
        return false
    }


    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 1)) return
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                val age = getAge(state)
                if (age <= this.maxAge && rand.nextDouble() < 0.12) {
                    this.grow(worldIn, pos, state)
                }
            }
        }
    }

    override fun isReplaceable(worldIn: IBlockAccess, pos: BlockPos): Boolean {
        return true
    }

    override fun isPassable(worldIn: IBlockAccess, pos: BlockPos): Boolean {
        return true
    }

    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState): Boolean {
        return false
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        entityIn.motionX = entityIn.motionX / ((getAge(state) + 1) * 0.1 + 1)
        entityIn.motionZ = entityIn.motionZ / ((getAge(state) + 1) * 0.1 + 1)
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        if (!canBlockStay(worldIn, pos, state)) {
            dropBlockAsItem(worldIn, pos, state, 0)
            worldIn.setBlockToAir(pos)
        }
    }

    // Placement
    override fun canPlaceBlockOnSide(worldIn: World, pos: BlockPos, side: EnumFacing): Boolean {
        return canBlockStay(worldIn, pos, defaultState)
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        return canBlockStay(worldIn, pos, defaultState)
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean {
        val down = worldIn.getBlockState(pos.down())
        val down2 = worldIn.getBlockState(pos.down(2))

        if (down.material in ALLOWED_SOILS || down.block == Blocks.DIRT) {
            return true
        } else return down.block == this && getAge(down) == this.maxAge && return down2.material in ALLOWED_SOILS || down2.block == Blocks.DIRT
    }


    // Growing
    override fun getBonemealAgeIncrease(worldIn: World): Int {
        return super.getBonemealAgeIncrease(worldIn) / this.maxAge
    }

    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState, isClient: Boolean): Boolean {
        return (getAge(state) < this.maxAge + 1) && worldIn.getBlockState(pos.up()).block != this
    }

    override fun grow(worldIn: World, pos: BlockPos, state: IBlockState) {
        var newAge = getAge(state) + getBonemealAgeIncrease(worldIn)
        val maxAge = this.maxAge
        if (newAge > maxAge) {
            newAge = maxAge
            if (worldIn.isAirBlock(pos.up()) && canBlockStay(worldIn, pos.up(), state)) {
                worldIn.setBlockState(pos.up(), withAge(0), 2)
            }
        }
        worldIn.setBlockState(pos, withAge(newAge), 2)
    }


    // Drops
    override fun getDrops(
        drops: NonNullList<ItemStack?>,
        world: IBlockAccess?,
        pos: BlockPos?,
        state: IBlockState?,
        fortune: Int
    ) {
        if (RANDOM.nextInt(8) != 0) return
        val seed = ForgeHooks.getGrassSeed(RANDOM, fortune)
        if (!seed.isEmpty) drops.add(seed)
    }

    override fun getItemDropped(state: IBlockState?, rand: Random?, fortune: Int): Item? {
        return null
    }

    override fun quantityDroppedWithBonus(fortune: Int, random: Random): Int {
        return 1 + random.nextInt(fortune * 2 + 1)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun harvestBlock(
        worldIn: World,
        player: EntityPlayer,
        pos: BlockPos,
        state: IBlockState,
        te: TileEntity?,
        stack: ItemStack
    ) {
        if (!worldIn.isRemote && stack.item === Items.SHEARS) {
            player.addStat(StatList.getBlockStats(this))
            spawnAsEntity(worldIn, pos, ItemStack(this, 1))
        } else {
            super.harvestBlock(worldIn, player, pos, state, te, stack)
        }
    }

}
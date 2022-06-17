package teksturepako.greenery.common.block.tallgrass

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.NonNullList
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.block.GreeneryCropBase
import java.util.*

abstract class AbstractTallGrass(name: String) : GreeneryCropBase() {

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

    init {
        setRegistryName(name)
        translationKey = name
        soundType = SoundType.PLANT
        creativeTab = Greenery.creativeTab
    }

    override fun getSeed(): Item {
        return itemBlock
    }

    override fun getCrop(): Item {
        return itemBlock
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean {
        val down = worldIn.getBlockState(pos.down())
        val down2 = worldIn.getBlockState(pos.down(2))

        return if (worldIn.isAirBlock(pos) || worldIn.getBlockState(pos).block == this) {
            ((down.material in ALLOWED_SOILS || down.block == Blocks.DIRT)
                    || (down.block == this && getAge(down) == this.maxAge && down2.material in ALLOWED_SOILS))
        } else false
    }


    // Growing
    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState, isClient: Boolean): Boolean {
        return when {
            worldIn.getBlockState(pos.up()).block == this -> false
            worldIn.getBlockState(pos.down()).block == this -> (getAge(state) < this.maxAge)
            else -> true
        }
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
        drops: NonNullList<ItemStack>,
        world: IBlockAccess,
        pos: BlockPos,
        state: IBlockState,
        fortune: Int
    ) {
        if (RANDOM.nextInt(8) != 0) return
        val seed = ForgeHooks.getGrassSeed(RANDOM, fortune)
        if (!seed.isEmpty) drops.add(seed)
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
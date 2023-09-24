@file:Suppress("MemberVisibilityCanBePrivate")

package teksturepako.greenery.common.block

import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teksturepako.greenery.Greenery

class BlockDriedKelp : Block(Material.GRASS, MapColor.GRAY)
{
    companion object
    {
        const val NAME = "dried_kelp_block"
    }

    lateinit var itemBlock: Item

    init
    {
        setRegistryName(NAME)
        translationKey = "${Greenery.MODID}.$NAME"
        soundType = SoundType.PLANT
        creativeTab = Greenery.creativeTab

        blockHardness = 0.5f
    }

    fun createItemBlock(): Item
    {
        itemBlock = object : ItemBlock(this)
        {
            init
            {
                registryName = this@BlockDriedKelp.registryName
                translationKey = this@BlockDriedKelp.translationKey
            }

            override fun getItemBurnTime(itemStack: ItemStack): Int = 4000
        }

        return itemBlock
    }

    @SideOnly(Side.CLIENT)
    fun registerItemModel()
    {
        Greenery.proxy.registerItemBlockRenderer(itemBlock, 0, registryName.toString())
    }

    override fun isFlammable(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Boolean = true
    override fun getFlammability(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Int = 30
    override fun getHarvestTool(state: IBlockState): String = "hoe"
}
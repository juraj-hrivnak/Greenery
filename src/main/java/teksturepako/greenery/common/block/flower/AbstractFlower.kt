package teksturepako.greenery.common.block.flower

import net.minecraft.block.BlockBush
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teksturepako.greenery.Greenery
import teksturepako.greenery.ModConfig

abstract class AbstractFlower(name: String, val config: ModConfig.FloraConfig) : BlockBush(Material.PLANTS) {
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

    override fun getOffsetType(): EnumOffsetType {
        return EnumOffsetType.XZ
    }

    override fun canPlaceBlockOnSide(worldIn: World, pos: BlockPos, side: EnumFacing): Boolean {
        return canBlockStay(worldIn, pos, defaultState)
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        return canBlockStay(worldIn, pos, defaultState)
    }

    open fun isBiomeValid(biome: Biome): Boolean {
        return true
    }
}
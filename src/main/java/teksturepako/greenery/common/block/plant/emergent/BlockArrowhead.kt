package teksturepako.greenery.common.block.plant.emergent

import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.item.Item
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.registry.ModItems

class BlockArrowhead : AbstractEmergentPlant(NAME)
{

    companion object
    {
        const val NAME = "arrowhead"
        const val REGISTRY_NAME = "${Greenery.MODID}:$NAME"

        val AGE: PropertyInteger = PropertyInteger.create("age", 0, 3)
    }

    init
    {
        defaultState = blockState.baseState.withProperty(AGE, 0)
    }

    override fun getAgeProperty(): PropertyInteger
    {
        return AGE
    }

    override fun getMaxAge(): Int
    {
        return 3
    }

    override fun getSeed(): Item
    {
        return ModItems.itemBlockArrowhead
    }

    override fun getCrop(): Item
    {
        return ModItems.itemBlockArrowhead
    }

    override fun createBlockState(): BlockStateContainer
    {
        return BlockStateContainer(this, AGE)
    }

}

class ItemBlockArrowhead : AbstractEmergentItemBlock(BlockArrowhead.NAME, ModBlocks.blockArrowhead)

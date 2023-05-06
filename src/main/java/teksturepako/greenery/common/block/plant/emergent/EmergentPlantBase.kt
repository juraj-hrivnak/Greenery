package teksturepako.greenery.common.block.plant.emergent

import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.item.Item

abstract class EmergentPlantBase(name: String) : AbstractEmergentPlant(name)
{
    companion object
    {
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
        return itemBlock
    }

    override fun getCrop(): Item
    {
        return itemBlock
    }

    override fun createBlockState(): BlockStateContainer
    {
        return BlockStateContainer(this, AGE)
    }
}
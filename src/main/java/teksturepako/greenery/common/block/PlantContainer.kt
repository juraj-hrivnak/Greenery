package teksturepako.greenery.common.block

import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.BlockStateContainer
import teksturepako.greenery.common.block.plant.GreeneryPlant

fun GreeneryPlant.plantContainer(vararg properties: IProperty<*>): BlockStateContainer =
    BlockStateContainer(this, *properties)

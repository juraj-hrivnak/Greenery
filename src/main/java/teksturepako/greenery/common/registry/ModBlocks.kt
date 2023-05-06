@file:Suppress("MemberVisibilityCanBePrivate")

package teksturepako.greenery.common.registry

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.registries.IForgeRegistry
import teksturepako.greenery.Greenery.plants
import teksturepako.greenery.common.block.BlockDriedKelp
import teksturepako.greenery.common.block.BlockGrass

object ModBlocks
{
    val blockGrass = BlockGrass()
    val blockDriedKelp = BlockDriedKelp()

    fun register(registry: IForgeRegistry<Block>)
    {
        for (plant in plants) registry.register(plant)

        registry.register(blockGrass)
        registry.register(blockDriedKelp)
    }

    fun registerItemBlocks(registry: IForgeRegistry<Item>)
    {
        for (plant in plants) registry.register(plant.createItemBlock())

        registry.register(blockGrass.createItemBlock())
        registry.register(blockDriedKelp.createItemBlock())
    }

    @SideOnly(Side.CLIENT)
    fun registerModels()
    {
        for (plant in plants) plant.registerItemModel()

        blockGrass.registerItemModel()
        blockDriedKelp.registerItemModel()
    }

    @SideOnly(Side.CLIENT)
    fun registerBlockColorHandlers(event: ColorHandlerEvent.Block)
    {
        for (plant in plants)
        {
            if (plant.hasTintIndex) plant.registerBlockColorHandler(event)
        }

        blockGrass.registerBlockColorHandler(event)
    }

    @SideOnly(Side.CLIENT)
    fun registerItemBlockColorHandlers(event: ColorHandlerEvent.Item)
    {
        for (plant in plants)
        {
            if (plant.hasTintIndex) plant.registerItemColorHandler(event)
        }

        blockGrass.registerItemColorHandler(event)
    }
}
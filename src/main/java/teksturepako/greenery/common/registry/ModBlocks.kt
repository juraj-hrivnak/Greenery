@file:Suppress("MemberVisibilityCanBePrivate")

package teksturepako.greenery.common.registry

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.registries.IForgeRegistry
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.block.BlockDriedKelp
import teksturepako.greenery.common.block.BlockGrass
import teksturepako.greenery.common.block.plant.GreeneryPlant
import teksturepako.greenery.common.block.plant.emergent.EmergentPlantBase
import teksturepako.greenery.common.block.plant.submerged.BlockKelp
import teksturepako.greenery.common.block.plant.submerged.BlockSeagrass
import teksturepako.greenery.common.block.plant.submerged.BlockWatermilfoil
import teksturepako.greenery.common.block.plant.upland.tall.TallPlantBase
import teksturepako.greenery.common.config.Config

object ModBlocks
{
    val blockGrass = BlockGrass()
    val blockDriedKelp = BlockDriedKelp()

    // Submerged
    val blockSeagrass = BlockSeagrass()
    val blockWatermilfoil = BlockWatermilfoil()
    val blockKelp = BlockKelp()

    // Emergent
    val blockCattail = object : EmergentPlantBase("cattail")
    {
        override val worldGenConfig get() = Config.plant.emergent.cattail.worldGen.toMutableList()
        override val isSolid get() = false
        override val isHarmful get() = false
    }

    val blockArrowhead = object : EmergentPlantBase("arrowhead")
    {
        override val worldGenConfig get() = Config.plant.emergent.arrowhead.worldGen.toMutableList()
        override val isSolid get() = false
        override val isHarmful get() = false
    }

    val blockPickerelweed = object : EmergentPlantBase("pickerelweed")
    {
        override val worldGenConfig get() = Config.plant.emergent.pickerelweed.worldGen.toMutableList()
        override val isSolid get() = false
        override val isHarmful get() = false
    }

    // Upland Tall
    val blockFoxtail = object : TallPlantBase("foxtail")
    {
        override val worldGenConfig get() = Config.plant.upland.tall.foxtail.worldGen.toMutableList()
        override val drops get() = Config.plant.upland.tall.foxtail.drops.toMutableList()
        override val isSolid get() = false
        override val isHarmful get() = false
    }

    val blockEagleFern = object : TallPlantBase("eagle_fern")
    {
        override val worldGenConfig get() = Config.plant.upland.tall.eagleFern.worldGen.toMutableList()
        override val drops get() = Config.plant.upland.tall.eagleFern.drops.toMutableList()
        override val isSolid get() = false
        override val isHarmful get() = false
    }

    val blockRyegrass = object : TallPlantBase("ryegrass")
    {
        override val worldGenConfig get() = Config.plant.upland.tall.ryegrass.worldGen.toMutableList()
        override val drops get() = Config.plant.upland.tall.ryegrass.drops.toMutableList()
        override val isSolid get() = false
        override val isHarmful get() = false
    }

    val blockNettle = object : TallPlantBase("nettle")
    {
        override val worldGenConfig get() = Config.plant.upland.tall.nettle.worldGen.toMutableList()
        override val drops get() = Config.plant.upland.tall.nettle.drops.toMutableList()
        override val isSolid get() = false
        override val isHarmful get() = true
    }

    val blockBarley = object : TallPlantBase("barley")
    {
        override val worldGenConfig get() = Config.plant.upland.tall.barley.worldGen.toMutableList()
        override val drops get() = Config.plant.upland.tall.barley.drops.toMutableList()
        override val isSolid get() = false
        override val isHarmful get() = false
    }

    private val plants: MutableList<GreeneryPlant> = Greenery.loadPlants()

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
        blockArrowhead.registerBlockColorHandler(event)
        blockFoxtail.registerBlockColorHandler(event)
        blockEagleFern.registerBlockColorHandler(event)
        blockRyegrass.registerBlockColorHandler(event)
        blockGrass.registerBlockColorHandler(event)
    }
}
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
        override var worldGenConfig = Config.plant.emergent.cattail.worldGen.toMutableList()
        override var isSolid = false
        override var isHarmful = false
    }

    val blockArrowhead = object : EmergentPlantBase("arrowhead")
    {
        override var worldGenConfig = Config.plant.emergent.arrowhead.worldGen.toMutableList()
        override var isSolid = false
        override var isHarmful = false
    }

    val blockPickerelweed = object : EmergentPlantBase("pickerelweed")
    {
        override var worldGenConfig = Config.plant.emergent.pickerelweed.worldGen.toMutableList()
        override var isSolid = false
        override var isHarmful = false
    }

    // Upland Tall
    val blockFoxtail = object : TallPlantBase("foxtail")
    {
        override var worldGenConfig = Config.plant.upland.tall.foxtail.worldGen.toMutableList()
        override var drops = Config.plant.upland.tall.foxtail.drops.toMutableList()
        override var isSolid = false
        override var isHarmful = false
    }

    val blockEagleFern = object : TallPlantBase("eagle_fern")
    {
        override var worldGenConfig = Config.plant.upland.tall.eagleFern.worldGen.toMutableList()
        override var drops = Config.plant.upland.tall.eagleFern.drops.toMutableList()
        override var isSolid = false
        override var isHarmful = false
    }

    val blockRyegrass = object : TallPlantBase("ryegrass")
    {
        override var worldGenConfig = Config.plant.upland.tall.ryegrass.worldGen.toMutableList()
        override var drops = Config.plant.upland.tall.ryegrass.drops.toMutableList()
        override var isSolid = false
        override var isHarmful = false
    }

    val blockNettle = object : TallPlantBase("nettle")
    {
        override var worldGenConfig = Config.plant.upland.tall.nettle.worldGen.toMutableList()
        override var drops = Config.plant.upland.tall.nettle.drops.toMutableList()
        override var isSolid = false
        override var isHarmful = true
    }

    val blockBarley = object : TallPlantBase("barley")
    {
        override var worldGenConfig = Config.plant.upland.tall.barley.worldGen.toMutableList()
        override var drops = Config.plant.upland.tall.barley.drops.toMutableList()
        override var isSolid = false
        override var isHarmful = false
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
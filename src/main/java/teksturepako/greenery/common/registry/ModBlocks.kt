package teksturepako.greenery.common.registry

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.registries.IForgeRegistry
import teksturepako.greenery.common.block.BlockDriedKelp
import teksturepako.greenery.common.block.BlockGrass
import teksturepako.greenery.common.block.plant.emergent.EmergentPlantBase
import teksturepako.greenery.common.block.plant.submerged.BlockKelp
import teksturepako.greenery.common.block.plant.submerged.BlockSeagrass
import teksturepako.greenery.common.block.plant.submerged.BlockWatermilfoil
import teksturepako.greenery.common.block.plant.upland.tall.TallPlantBase
import teksturepako.greenery.common.config.Config

object ModBlocks
{
    val blockSeagrass = BlockSeagrass()
    val blockWatermilfoil = BlockWatermilfoil()
    val blockKelp = BlockKelp()
    val blockDriedKelp = BlockDriedKelp()

    val blockCattail = EmergentPlantBase(
        "cattail", Config.plant.emergent.cattail.worldGen.toMutableList()
    )
    val blockArrowhead = EmergentPlantBase(
        "arrowhead", Config.plant.emergent.arrowhead.worldGen.toMutableList()
    )
    val blockPickerelweed = EmergentPlantBase(
        "pickerelweed", Config.plant.emergent.pickerelweed.worldGen.toMutableList()
    )
    val blockGrass = BlockGrass()

    val blockFoxtail = TallPlantBase(
        "foxtail", Config.plant.upland.tall.foxtail.drops.toMutableList(), false, Config.plant.upland.tall.foxtail.worldGen.toMutableList()
    )
    val blockEagleFern = TallPlantBase(
        "eagle_fern", Config.plant.upland.tall.eagleFern.drops.toMutableList(), false, Config.plant.upland.tall.eagleFern.worldGen.toMutableList()
    )
    val blockRyegrass = TallPlantBase(
        "ryegrass", Config.plant.upland.tall.ryegrass.drops.toMutableList(), false, Config.plant.upland.tall.ryegrass.worldGen.toMutableList()
    )
    val blockNettle = TallPlantBase(
        "nettle", Config.plant.upland.tall.nettle.drops.toMutableList(), true, Config.plant.upland.tall.nettle.worldGen.toMutableList()
    )
    val blockBarley = TallPlantBase(
        "barley", Config.plant.upland.tall.barley.drops.toMutableList(), false, Config.plant.upland.tall.barley.worldGen.toMutableList()
    )

    fun register(registry: IForgeRegistry<Block>)
    {
        registry.register(blockSeagrass)
        registry.register(blockWatermilfoil)
        registry.register(blockCattail)
        registry.register(blockArrowhead)
        registry.register(blockPickerelweed)
        registry.register(blockGrass)
        registry.register(blockFoxtail)
        registry.register(blockEagleFern)
        registry.register(blockRyegrass)
        registry.register(blockNettle)
        registry.register(blockBarley)
        registry.register(blockKelp)
        registry.register(blockDriedKelp)
    }

    fun registerItemBlocks(registry: IForgeRegistry<Item>)
    {
        registry.register(blockSeagrass.createItemBlock())
        registry.register(blockWatermilfoil.createItemBlock())
        registry.register(blockCattail.createItemBlock())
        registry.register(blockArrowhead.createItemBlock())
        registry.register(blockPickerelweed.createItemBlock())
        registry.register(blockGrass.createItemBlock())
        registry.register(blockFoxtail.createItemBlock())
        registry.register(blockEagleFern.createItemBlock())
        registry.register(blockRyegrass.createItemBlock())
        registry.register(blockNettle.createItemBlock())
        registry.register(blockBarley.createItemBlock())
        registry.register(blockKelp.createItemBlock())
        registry.register(blockDriedKelp.createItemBlock())
    }

    @SideOnly(Side.CLIENT)
    fun registerModels()
    {
        blockSeagrass.registerItemModel()
        blockWatermilfoil.registerItemModel()
        blockCattail.registerItemModel()
        blockArrowhead.registerItemModel()
        blockPickerelweed.registerItemModel()
        blockGrass.registerItemModel()
        blockFoxtail.registerItemModel()
        blockEagleFern.registerItemModel()
        blockRyegrass.registerItemModel()
        blockNettle.registerItemModel()
        blockBarley.registerItemModel()
        blockKelp.registerItemModel()
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
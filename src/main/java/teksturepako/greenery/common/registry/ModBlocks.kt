package teksturepako.greenery.common.registry

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.registries.IForgeRegistry
import teksturepako.greenery.common.block.BlockDriedKelp
import teksturepako.greenery.common.block.crop.BlockArrowhead
import teksturepako.greenery.common.block.crop.BlockCattail
import teksturepako.greenery.common.block.grass.BlockGrass
import teksturepako.greenery.common.block.plant.freshwater.BlockRivergrass
import teksturepako.greenery.common.block.plant.saltwater.BlockKelp
import teksturepako.greenery.common.block.plant.saltwater.BlockSeagrass
import teksturepako.greenery.common.block.tallgrass.BlockRyegrass
import teksturepako.greenery.common.block.tallgrass.BlockTallFern
import teksturepako.greenery.common.block.tallgrass.BlockTallGrass

object ModBlocks {

    val blockSeagrass = BlockSeagrass()
    val blockRivergrass = BlockRivergrass()
    val blockKelp = BlockKelp()
    val blockDriedKelp = BlockDriedKelp()

    val blockCattail = BlockCattail()
    val blockArrowhead = BlockArrowhead()

    val blockGrass = BlockGrass()

    val blockTallGrass = BlockTallGrass()
    val blockTallFern = BlockTallFern()
    val blockRyegrass = BlockRyegrass()

    fun register(registry: IForgeRegistry<Block>) {

        registry.register(blockSeagrass)

        registry.register(blockRivergrass)

        registry.register(blockCattail)
        registry.register(blockArrowhead)

        registry.register(blockGrass)
        registry.register(blockTallGrass)
        registry.register(blockTallFern)
        registry.register(blockRyegrass)


        registry.register(blockKelp)
        registry.register(blockDriedKelp)
    }

    fun registerItemBlocks(registry: IForgeRegistry<Item>) {
        registry.register(blockSeagrass.createItemBlock())

        registry.register(blockRivergrass.createItemBlock())
        registry.register(blockGrass.createItemBlock())
        registry.register(blockTallGrass.createItemBlock())
        registry.register(blockTallFern.createItemBlock())
        registry.register(blockRyegrass.createItemBlock())

        registry.register(blockKelp.createItemBlock())
        registry.register(blockDriedKelp.createItemBlock())
    }

    @SideOnly(Side.CLIENT)
    fun registerModels() {
        blockSeagrass.registerItemModel()

        blockRivergrass.registerItemModel()
        blockGrass.registerItemModel()
        blockTallGrass.registerItemModel()
        blockTallFern.registerItemModel()
        blockRyegrass.registerItemModel()

        blockKelp.registerItemModel()
        blockDriedKelp.registerItemModel()
    }

    @SideOnly(Side.CLIENT)
    fun registerBlockColorHandlers(event: ColorHandlerEvent.Block) {
        blockArrowhead.registerBlockColorHandler(event)
        blockTallGrass.registerBlockColorHandler(event)
        blockTallFern.registerBlockColorHandler(event)
        blockRyegrass.registerBlockColorHandler(event)
        blockGrass.registerBlockColorHandler(event)
    }

}
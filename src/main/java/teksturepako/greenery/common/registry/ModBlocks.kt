package teksturepako.greenery.common.registry

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.registries.IForgeRegistry
import teksturepako.greenery.ModConfig
import teksturepako.greenery.common.block.BlockDriedKelp
import teksturepako.greenery.common.block.crop.BlockArrowhead
import teksturepako.greenery.common.block.crop.BlockCattail
import teksturepako.greenery.common.block.flower.BlockCornflower
import teksturepako.greenery.common.block.flower.BlockLilyOfTheValley
import teksturepako.greenery.common.block.flower.BlockWitherRose
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
    val blockLilyOfTheValley = BlockLilyOfTheValley()
    val blockCornflower = BlockCornflower()
    val blockWitherRose = BlockWitherRose()
    val blockDriedKelp = BlockDriedKelp()

    val blockCattail = BlockCattail()
    val blockArrowhead = BlockArrowhead()

    val blockGrass = BlockGrass()

    val blockTallGrass = BlockTallGrass()
    val blockTallFern = BlockTallFern()
    val blockRyegrass = BlockRyegrass()

    fun register(registry: IForgeRegistry<Block>) {

        if (ModConfig.Seagrass.enabled) registry.register(blockSeagrass)

        registry.register(blockRivergrass)

        registry.register(blockCattail)
        registry.register(blockArrowhead)

        registry.register(blockGrass)
        registry.register(blockTallGrass)
        registry.register(blockTallFern)
        registry.register(blockRyegrass)


        if (ModConfig.Kelp.enabled) {
            registry.register(blockKelp)
            if (ModConfig.Kelp.driedKelpEnabled) {
                registry.register(blockDriedKelp)
            }
        }

        if (ModConfig.Cornflower.enabled) registry.register(blockCornflower)
        if (ModConfig.LilyOfTheValley.enabled) registry.register(blockLilyOfTheValley)
        if (ModConfig.WitherRose.enabled) registry.register(blockWitherRose)
    }

    fun registerItemBlocks(registry: IForgeRegistry<Item>) {
        if (ModConfig.Seagrass.enabled) registry.register(blockSeagrass.createItemBlock())

        registry.register(blockRivergrass.createItemBlock())
        registry.register(blockGrass.createItemBlock())
        registry.register(blockTallGrass.createItemBlock())
        registry.register(blockTallFern.createItemBlock())
        registry.register(blockRyegrass.createItemBlock())


        if (ModConfig.Kelp.enabled) {
            registry.register(blockKelp.createItemBlock())
            if (ModConfig.Kelp.driedKelpEnabled) {
                registry.register(blockDriedKelp.createItemBlock())
            }
        }


        if (ModConfig.Cornflower.enabled) registry.register(blockCornflower.createItemBlock())
        if (ModConfig.LilyOfTheValley.enabled) registry.register(blockLilyOfTheValley.createItemBlock())
        if (ModConfig.WitherRose.enabled) registry.register(blockWitherRose.createItemBlock())
    }

    @SideOnly(Side.CLIENT)
    fun registerModels() {
        if (ModConfig.Seagrass.enabled) blockSeagrass.registerItemModel()

        blockRivergrass.registerItemModel()
        blockGrass.registerItemModel()
        blockTallGrass.registerItemModel()
        blockTallFern.registerItemModel()
        blockRyegrass.registerItemModel()

        if (ModConfig.Kelp.enabled) {
            blockKelp.registerItemModel()
            if (ModConfig.Kelp.driedKelpEnabled) {
                blockDriedKelp.registerItemModel()
            }
        }

        if (ModConfig.Cornflower.enabled) blockCornflower.registerItemModel()
        if (ModConfig.LilyOfTheValley.enabled) blockLilyOfTheValley.registerItemModel()
        if (ModConfig.WitherRose.enabled) blockWitherRose.registerItemModel()
    }

    @SideOnly(Side.CLIENT)
    fun registerColorHandlers(event: ColorHandlerEvent.Block) {
        blockArrowhead.registerColorHandler(event)
        blockTallGrass.registerColorHandler(event)
        blockTallFern.registerColorHandler(event)
        blockRyegrass.registerColorHandler(event)
        blockGrass.registerColorHandler(event)
    }

}
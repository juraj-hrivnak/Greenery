package teksturepako.greenery

import com.ferreusveritas.dynamictrees.systems.DirtHelper
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.SoundEvent
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.IWorldGenerator
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.Logger
import teksturepako.greenery.client.ModCreativeTab
import teksturepako.greenery.common.handler.ModFuelHandler
import teksturepako.greenery.common.recipe.ModRecipes
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.registry.ModItems
import teksturepako.greenery.common.registry.ModSoundEvents
import teksturepako.greenery.common.util.WorldGenUtil.parseValidBiomeTypes
import teksturepako.greenery.common.world.IGreeneryWorldGenerator
import teksturepako.greenery.common.world.WorldGenHook
import teksturepako.greenery.common.world.crop.WorldGenArrowhead
import teksturepako.greenery.common.world.crop.WorldGenCattail
import teksturepako.greenery.common.world.grass.WorldGenFerns
import teksturepako.greenery.common.world.grass.WorldGenTallGrass
import teksturepako.greenery.proxy.IProxy


@Mod(
    modid = Greenery.MODID,
    name = Greenery.NAME,
    version = Greenery.VERSION,
    dependencies = Greenery.DEPENDENCIES,
    acceptedMinecraftVersions = Greenery.ACCEPTED_MINECRAFT_VERSIONS,
    modLanguageAdapter = Greenery.ADAPTER
)

@Mod.EventBusSubscriber
object Greenery {
    const val MODID = "greenery"
    const val NAME = "Greenery"
    const val VERSION = "1.0"
    const val DEPENDENCIES = "required-after:forgelin@[1.8.4,);before:simpledifficulty;after:dynamictrees"
    const val ACCEPTED_MINECRAFT_VERSIONS = "[1.12,1.12.2,)"
    const val ADAPTER = "net.shadowfacts.forgelin.KotlinAdapter"

    const val SERVER_PROXY = "teksturepako.greenery.proxy.ServerProxy"
    const val CLIENT_PROXY = "teksturepako.greenery.proxy.ClientProxy"

    val creativeTab = ModCreativeTab()

    private fun dynamicTreesLoaded(): Boolean = (Loader.isModLoaded("dynamictrees"))

    @SidedProxy(serverSide = SERVER_PROXY, clientSide = CLIENT_PROXY)
    lateinit var proxy: IProxy
    lateinit var logger: Logger

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
        proxy.preInit(event)
    }

    @Mod.EventHandler
    @Suppress("DEPRECATION")
    fun init(event: FMLInitializationEvent) {
        proxy.init(event)
        GameRegistry.registerWorldGenerator(WorldGenHook(), 0)
        GameRegistry.registerFuelHandler(ModFuelHandler())
        ModRecipes.register()
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy.postInit(event)
        ModItems.initOreDictionary()
    }

    @SubscribeEvent
    @JvmStatic
    fun onRegisterBlocks(event: RegistryEvent.Register<Block>) {
        logger.info("Registering blocks")
        ModBlocks.register(event.registry)
        if (dynamicTreesLoaded()) {
            DirtHelper.registerSoil(ModBlocks.blockGrass, DirtHelper.DIRTLIKE)
        }
    }

    @SubscribeEvent
    @JvmStatic fun onRegisterItems(event: RegistryEvent.Register<Item>) {
        logger.info("Registering items")
        ModBlocks.registerItemBlocks(event.registry)
        ModItems.register(event.registry)
    }

    @SubscribeEvent
    @JvmStatic fun onRegisterSoundEvents(event: RegistryEvent.Register<SoundEvent>) {
        logger.info("Registering sounds")
        ModSoundEvents.register(event.registry)
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    @JvmStatic
    fun onRegisterBlockColorHandlers(event: ColorHandlerEvent.Block) {
        logger.info("Registering Block Color Handlers")
        proxy.registerBlockColourHandlers(ModBlocks.blockArrowhead, event)
        proxy.registerBlockColourHandlers(ModBlocks.blockTallGrass, event)
        proxy.registerBlockColourHandlers(ModBlocks.blockTallFern, event)
        proxy.registerBlockColourHandlers(ModBlocks.blockRyegrass, event)
        proxy.registerBlockColourHandlers(ModBlocks.blockGrass, event)
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    @JvmStatic
    fun onRegisterItemColorHandlers(event: ColorHandlerEvent.Item) {
        logger.info("Registering Item Color Handlers")
        proxy.registerItemColourHandlers(ModItems.itemBlockArrowhead, event)
        proxy.registerItemColourHandlers(ModBlocks.blockTallGrass.itemBlock, event)
        proxy.registerItemColourHandlers(ModBlocks.blockTallFern.itemBlock, event)
        proxy.registerItemColourHandlers(ModBlocks.blockRyegrass.itemBlock, event)
        proxy.registerItemColourHandlers(ModBlocks.blockGrass.itemBlock, event)
    }

    val generators: MutableList<IGreeneryWorldGenerator> = ArrayList()

    fun loadGenerators(): MutableList<IGreeneryWorldGenerator> {
        if (generators.isEmpty()) {

            generators.add(WorldGenCattail())
            generators.add(WorldGenArrowhead())
            generators.add(WorldGenTallGrass())
            generators.add(WorldGenFerns())

            logger.info("Loading world generators:")
            for (generator in generators) {
                logger.info(
                    "> " + generator.javaClass.name
                        .replace("teksturepako.greenery.common.world.", "")
                )
                parseValidBiomeTypes(generator.validBiomeTypes)
            }

            logger.info("-------------------")
        }
        return generators
    }

}

@file:Suppress("MemberVisibilityCanBePrivate")

package teksturepako.greenery

import com.ferreusveritas.dynamictrees.systems.DirtHelper
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.SoundEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.*
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import org.apache.logging.log4j.Logger
import teksturepako.greenery.client.GreeneryCreativeTab
import teksturepako.greenery.common.block.plant.GreeneryPlant
import teksturepako.greenery.common.command.CommandGreenery
import teksturepako.greenery.common.config.json.arbBlock.ArbBlockData
import teksturepako.greenery.common.config.json.arbBlock.ArbBlockParser
import teksturepako.greenery.common.config.json.plant.PlantParser
import teksturepako.greenery.common.event.EventOldContentLoad
import teksturepako.greenery.common.event.EventWorldGen
import teksturepako.greenery.common.recipe.ModRecipes
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.registry.ModItems
import teksturepako.greenery.common.registry.ModSoundEvents
import teksturepako.greenery.common.util.ConfigUtil
import teksturepako.greenery.common.worldGen.*
import teksturepako.greenery.proxy.IProxy
import java.nio.file.Path
import kotlin.io.path.Path

@Mod(
    modid = Greenery.MODID,
    name = Greenery.NAME,
    version = Greenery.VERSION,
    dependencies = Greenery.DEPENDENCIES,
    acceptedMinecraftVersions = Greenery.ACCEPTED_MINECRAFT_VERSIONS,
    modLanguageAdapter = Greenery.ADAPTER
)
@Mod.EventBusSubscriber
object Greenery
{
    const val MODID = "greenery"
    const val NAME = "Greenery"
    const val VERSION = Tags.VERSION
    const val DEPENDENCIES = "required-after:forgelin_continuous@[1.8.21.0,);required-after:fluidlogged_api@[2.0.0,);after:dynamictrees;after:biomesoplenty"
    const val ACCEPTED_MINECRAFT_VERSIONS = "[1.12,1.12.2,)"
    const val ADAPTER = "io.github.chaosunity.forgelin.KotlinAdapter"

    const val SERVER_PROXY = "teksturepako.greenery.proxy.ServerProxy"
    const val CLIENT_PROXY = "teksturepako.greenery.proxy.ClientProxy"

    val creativeTab = GreeneryCreativeTab()

    val plants: MutableList<GreeneryPlant> = mutableListOf()
    val arbBlocks: MutableList<ArbBlockData> = mutableListOf()

    val plantGenerators: MutableList<IPlantGenerator> = mutableListOf()
    val arbBlockGenerators: MutableList<IArbBlockGenerator> = mutableListOf()

    @SidedProxy(serverSide = SERVER_PROXY, clientSide = CLIENT_PROXY)
    lateinit var proxy: IProxy
    lateinit var logger: Logger
    lateinit var configFolder: Path

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent)
    {
        logger = event.modLog
        proxy.preInit(event)

        configFolder = Path(event.modConfigurationDirectory.path, MODID)
        PlantParser.initDefaults()
        PlantParser.decodeData()

        ArbBlockParser.decodeOrReloadData()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent)
    {
        proxy.init(event)
        GameRegistry.registerWorldGenerator(WorldGenHook(), 0)
        ModRecipes.register()

        MinecraftForge.EVENT_BUS.register(EventOldContentLoad::class.java)
        MinecraftForge.TERRAIN_GEN_BUS.register(EventWorldGen::class.java)
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent)
    {
        proxy.postInit(event)
        ModItems.initOreDictionary()
    }

    @Mod.EventHandler
    fun serverLoad(event: FMLServerStartingEvent)
    {
        loadPlantGenerators(true)
        event.registerServerCommand(CommandGreenery())
    }

    @SubscribeEvent
    @JvmStatic
    fun onRegisterBlocks(event: RegistryEvent.Register<Block>)
    {
        logger.info("Registering blocks")
        ModBlocks.register(event.registry)
        if (Loader.isModLoaded("dynamictrees"))
        {
            DirtHelper.registerSoil(ModBlocks.blockGrass, DirtHelper.DIRTLIKE)
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun onRegisterItems(event: RegistryEvent.Register<Item>)
    {
        logger.info("Registering items")
        ModBlocks.registerItemBlocks(event.registry)
        ModItems.register(event.registry)
    }

    @SubscribeEvent
    @JvmStatic
    fun onRegisterSoundEvents(event: RegistryEvent.Register<SoundEvent>)
    {
        logger.info("Registering sounds")
        ModSoundEvents.register(event.registry)
    }

    fun loadPlantGenerators(printParsing: Boolean): MutableList<IPlantGenerator>
    {
        if (plantGenerators.isEmpty())
        {
            for (plant in plants)
            {
                plantGenerators.add(PlantGenerator(plant))
            }

            ConfigUtil.parseGenerators(plantGenerators.map { it.plant.localizedName to it.plant.worldGen }, printParsing)
        }
        return plantGenerators
    }

    fun loadArbBlockGenerators(printParsing: Boolean): MutableList<IArbBlockGenerator>
    {
        if (arbBlockGenerators.isEmpty())
        {
            for (block in arbBlocks)
            {
                arbBlockGenerators.add(ArbBlockGenerator(block.name, block.blockStates, block.worldGen, block.allowedSoils))
            }

            ConfigUtil.parseGenerators(arbBlockGenerators.map { it.name to it.worldGen }, printParsing)
        }
        return arbBlockGenerators
    }

    @Mod.EventHandler
    fun onServerStoppingEvent(event: FMLServerStoppingEvent?)
    {
        logger.info("Unloading world generators")
        plantGenerators.clear()
        arbBlockGenerators.clear()
    }
}

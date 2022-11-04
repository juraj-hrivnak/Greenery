package teksturepako.greenery.common.config;

import net.minecraftforge.common.config.Config;
import teksturepako.greenery.Greenery;
import teksturepako.greenery.common.config.Config.GlobalSettings;
import teksturepako.greenery.common.config.Config.PlantSettings;

import static net.minecraftforge.common.config.Config.Comment;
import static net.minecraftforge.common.config.Config.Name;

/**
 * Exposes Kotlin config to the ConfigManager.
 * Required because the config manager also picks up the INSTANCE field
 * that Kotlin object classes generate.
 */
@Config(modid = Greenery.MODID, name = Greenery.MODID + "/config")
public final class _Internal {
    @Name("Global settings")
    @Comment("Global settings")
    public static final GlobalSettings GLOBAL_SETTINGS = new GlobalSettings();

    @Name("Plant settings")
    @Comment("Plant settings")
    public static final PlantSettings PLANT_SETTINGS = new PlantSettings();
}

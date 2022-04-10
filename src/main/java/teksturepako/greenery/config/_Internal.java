package teksturepako.greenery.config;

import net.minecraftforge.common.config.Config;
import teksturepako.greenery.Greenery;
import teksturepako.greenery.config.Config.Generation;

import static net.minecraftforge.common.config.Config.Comment;
import static net.minecraftforge.common.config.Config.Name;

/**
 * Exposes Kotlin config to the ConfigManager.
 * Required because the config manager also picks up the INSTANCE field
 * that Kotlin object classes generate.
 */
@Config(modid = Greenery.MODID, name = Greenery.MODID + "/worldGen")
public final class _Internal {

    @Name("Generation")
    @Comment("The generation chances of all the plants.")
    public static final Generation generation = new Generation();
}
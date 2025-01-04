package teksturepako.greenery.common.config

import net.minecraftforge.common.config.Config.*

object Config
{
    val global: GlobalSettings = _Internal.GLOBAL_SETTINGS

    class GlobalSettings
    {
        @Name("[1] Remove Grass")
        @Comment("Whether to remove the vanilla and Biomes O' Plenty grass or not.")
        @JvmField
        var removeGrass = true

        @Name("[2] Generate Plants From Bonemeal")
        @Comment("Whether to grow new plants when using bonemeal or not.")
        @JvmField
        var genPlantsFromBonemeal = true

        @Name("[3] Slowdown Modifier")
        @Comment("Slows down the movement through plants. Higher = slower.")
        @RangeInt(min = -100, max = 100)
        @JvmField
        var slowdownModifier = 0.1

        @Name("[4] Generate Default Configs")
        @Comment("Whether to generate default plant configs or not.")
        @JvmField
        var genDefaults = true

        @Name("[5] Generate Plants In Superflat")
        @Comment("Whether to generate plants in the superflat world type.")
        @JvmField
        var genInSuperflat = true

        @Name("[6] Print Debug Info")
        @Comment("Prints debug info to the log file.")
        @JvmField
        var printDebugInfo = true

        @Name("[7] Override Other Bonemeal Events")
        @Comment("Cancels the bonemeal event for other mods and vanilla plants.")
        @JvmField
        var overrideBonemealEvent = false
    }
}
package teksturepako.greenery.client

import net.minecraft.block.SoundType
import teksturepako.greenery.common.registry.ModSoundEvents

object ModSoundTypes
{
    val SEAWEED = SoundType(
            1f,
            1f,
            ModSoundEvents.SEAWEED_BREAK,
            ModSoundEvents.SEAWEED_STEP,
            ModSoundEvents.SEAWEED_PLACE,
            ModSoundEvents.SEAWEED_HIT,
            ModSoundEvents.SEAWEED_FALL
    )
}
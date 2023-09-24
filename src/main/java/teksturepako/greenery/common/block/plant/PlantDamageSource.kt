package teksturepako.greenery.common.block.plant

import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.DamageSource
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation

open class PlantDamageSource : DamageSource("GreeneryPlant")
{
    companion object
    {
        class Prickly(private val sourceLocalizedName: String) : PlantDamageSource()
        {
            override fun getDeathMessage(entityLivingBaseIn: EntityLivingBase): ITextComponent
            {
                return TextComponentTranslation(
                    "death.attack.greenery.pricked", entityLivingBaseIn.getDisplayName(), sourceLocalizedName.lowercase()
                )
            }
        }
    }

    override fun isDifficultyScaled(): Boolean = true
}

package teksturepako.greenery.common.util

import net.minecraft.util.DamageSource

class ModDamageSource(damageTypeIn: String) : DamageSource(damageTypeIn) {
    companion object {
        val NETTLE = DamageSource("nettle")
    }
}
package teksturepako.greenery.common.world

import net.minecraftforge.fml.common.IWorldGenerator

interface IGreeneryWorldGenerator : IWorldGenerator {
    val validBiomeTypes: MutableList<String>
}
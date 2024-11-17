package teksturepako.greenery.common.config.parser

import net.minecraft.block.state.IBlockState
import teksturepako.greenery.common.util.MaterialUtil

object SoilParser
{
    fun fromAllowedSoils(allowedSoils: List<String>): List<String> = allowedSoils.map {
        "material:$it"
    }

    fun parse(inputs: List<String>): (IBlockState) -> Boolean
    {
        if (inputs.isEmpty()) return { false }

        if (inputs.size == 1) return fun(blockState: IBlockState): Boolean =
            parse(inputs.first())?.invoke(blockState) ?: false

        return fun(blockState: IBlockState): Boolean = inputs.any { input ->
            parse(input)?.invoke(blockState) == true
        }
    }

    private fun parse(input: String): ((IBlockState) -> Boolean)?
    {
        when
        {
            input.startsWith("material") -> return input
                .filter { !it.isWhitespace() }.trim()
                .split(":")
                .getOrNull(1)
                ?.let { materialString ->
                    { blockState: IBlockState ->
                        blockState.material in MaterialUtil.materialsOf(materialString)
                    }
                } ?: { false }

            input.startsWith("block") ->
            {
                val (blockString: String, blockStateString: String?) = input
                    .filter { !it.isWhitespace() }.trim()
                    .split("|")
                    .let {
                        it[0].removePrefix("block:") to it.getOrNull(1)
                    }

                return x@ { blockState: IBlockState ->
                    val regName: String = blockState.block.registryName?.toString() ?: return@x false

                    if (blockStateString != null)
                    {
                        regName == blockString && isBlockStateValid(blockState, blockStateString)
                    }
                    else
                    {
                        regName == blockString
                    }
                }
            }

            else -> return null
        }
    }
}
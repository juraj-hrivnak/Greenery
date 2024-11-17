package teksturepako.greenery.common.config.parser

import net.minecraft.block.state.IBlockState

fun isBlockStateValid(state: IBlockState, input: String): Boolean = input
    .split(",")
    .asSequence()
    .map { it.split("=") }
    .all { (key, value) ->
        state.properties.any { it.key.name == key && it.value.toString() == value }
    }
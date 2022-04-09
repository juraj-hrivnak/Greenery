//package azmalent.backportedflora.common.block.plant.freshwater
//
//import azmalent.backportedflora.BackportedFlora
//import azmalent.backportedflora.ModConfig
//import net.minecraft.block.BlockLiquid.LEVEL
//import net.minecraft.block.properties.PropertyEnum
//import net.minecraft.block.state.BlockStateContainer
//import net.minecraft.block.state.IBlockState
//import net.minecraft.util.IStringSerializable
//import net.minecraft.util.ResourceLocation
//import net.minecraft.util.math.BlockPos
//import net.minecraft.world.IBlockAccess
//import net.minecraft.world.World
//import java.util.*
//
//
//class BlockSeaweed : AbstractAquaticPlant(NAME) {
//    enum class SeaweedPosition : IStringSerializable {
//        SINGLE, BOTTOM, MIDDLE, TOP;
//
//        override fun getName(): String {
//            return name.toLowerCase()
//        }
//
//        override fun toString(): String {
//            return getName()
//        }
//    }
//
//    enum class SeaweedType : IStringSerializable {
//        KELP;
//
//        override fun getName(): String {
//            return name.toLowerCase()
//        }
//
//        override fun toString(): String {
//            return getName()
//        }
//    }
//
//    companion object {
//        const val NAME = "seaweed"
//        const val REGISTRY_NAME = "${BackportedFlora.MODID}:$NAME"
//
//        val VARIANT: PropertyEnum<SeaweedType> = PropertyEnum.create("variant", SeaweedType::class.java)
//        val POSITION: PropertyEnum<SeaweedPosition> = PropertyEnum.create("position", SeaweedPosition::class.java)
//    }
//
//    init {
//        defaultState = blockState.baseState
//                .withProperty(LEVEL, 15)
//                .withProperty(POSITION, SeaweedPosition.SINGLE)
//                .withProperty(VARIANT, SeaweedType.KELP)
//
//        tickRandomly = ModConfig.Kelp.growthEnabled
//    }
//
//    // map from state to meta and vice verca - note the LEVEL and POSITION properties are ignored
//    override fun getStateFromMeta(meta: Int): IBlockState? {
//        return this.defaultState.withProperty(VARIANT, SeaweedType.values()[meta])
//    }
//
//    override fun getMetaFromState(state: IBlockState): Int {
//        return (state.getValue(VARIANT) as SeaweedType).ordinal
//    }
//
//    override fun createBlockState(): BlockStateContainer {
//        return BlockStateContainer(this, LEVEL, POSITION, VARIANT)
//    }
//
//    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState? {
//        val seaweedAbove = worldIn.getBlockState(pos.up()).block === this
//        val seaweedBelow = worldIn.getBlockState(pos.down()).block === this
//        val position: SeaweedPosition = if (seaweedAbove && seaweedBelow) {
//            SeaweedPosition.MIDDLE
//        } else if (seaweedAbove) {
//            SeaweedPosition.BOTTOM
//        } else if (seaweedBelow) {
//            SeaweedPosition.TOP
//        } else {
//            SeaweedPosition.SINGLE
//        }
//        return state.withProperty(POSITION, position)
//    }
//
//    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean {
//        //Must have water above
//        val up = worldIn.getBlockState(pos.up())
//        if (up.block.registryName != REGISTRY.getObject(ResourceLocation("simpledifficulty", "purifiedwater")).registryName
//            && up.block.registryName != this.registryName) return false
//
//
//        //Must have kelp or valid soil below
//        val down = worldIn.getBlockState(pos.down())
//        if (down.block == this) {
//            return true
//        } else return down.material in ALLOWED_SOILS
//    }
//
//    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
//        //Try to grow if possible
//        if (rand.nextDouble() < 0.14) {
//            val up = worldIn.getBlockState(pos.up())
//            if (up.block == REGISTRY.getObject(ResourceLocation("simpledifficulty", "purifiedwater"))) {
//                val newBlockState = defaultState
//                if (canBlockStay(worldIn, pos.up(), newBlockState)) {
//                    worldIn.setBlockState(pos.up(), newBlockState)
//                }
//            }
//        }
//    }
//
//    private fun getTopPosition(worldIn: World, pos: BlockPos): BlockPos {
//        var topPos = pos
//        while(worldIn.getBlockState(topPos.up()).block == this) {
//            topPos = topPos.up()
//        }
//
//        return topPos
//    }
//
//
//    // IGrowable implementation
//    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState, isClient: Boolean): Boolean {
//        val topPos = getTopPosition(worldIn, pos)
//        return canBlockStay(worldIn, topPos.up(), state)
//    }
//
//    override fun grow(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState) {
//        val topPos = getTopPosition(worldIn, pos)
//
//        val newBlockState = defaultState
//        worldIn.setBlockState(topPos.up(), newBlockState)
//    }
//}
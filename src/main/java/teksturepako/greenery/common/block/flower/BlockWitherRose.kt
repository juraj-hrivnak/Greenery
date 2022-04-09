package teksturepako.greenery.common.block.flower

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Blocks
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary
import teksturepako.greenery.Greenery
import teksturepako.greenery.ModConfig

class BlockWitherRose : AbstractFlower(NAME, ModConfig.WitherRose) {
    companion object {
        const val NAME = "wither_rose"
        const val REGISTRY_NAME = "${Greenery.MODID}:$NAME"
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean {
        val soil = worldIn.getBlockState(pos.down()).block
        return soil == Blocks.SOUL_SAND || soil == Blocks.NETHERRACK
    }

    override fun isBiomeValid(biome: Biome): Boolean {
        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        if (ModConfig.WitherRose.causesWither && entityIn is EntityLivingBase) {
            entityIn.addPotionEffect(PotionEffect(MobEffects.WITHER, 120))
            entityIn.attackEntityFrom(DamageSource.WITHER, 1.0f)
        }
    }
}
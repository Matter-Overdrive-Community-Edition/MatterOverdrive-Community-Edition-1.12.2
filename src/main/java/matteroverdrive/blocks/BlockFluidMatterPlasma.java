
package matteroverdrive.blocks;

import matteroverdrive.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidMatterPlasma extends BlockFluidClassic {
	public BlockFluidMatterPlasma(Fluid fluid, Material material) {
		super(fluid, material);
		setTranslationKey("matter_plasma");
		setRegistryName(new ResourceLocation(Reference.MOD_ID, "matter_plasma"));
	}

	@Override
	public boolean canDisplace(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return !state.getMaterial().isLiquid() && super.canDisplace(world, pos);
	}

	@Override
	public boolean displaceIfPossible(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return !state.getMaterial().isLiquid() && super.displaceIfPossible(world, pos);
	}
}

package matteroverdrive.blocks;

import matteroverdrive.api.internal.OreDictItem;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.MatterOverdrive;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockDecorativeColored extends BlockDecorative implements OreDictItem {
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

	public BlockDecorativeColored(Material material, String name, float hardness, int harvestLevel, float resistance,
			int mapColor) {

		super(material, name, hardness, harvestLevel, resistance, mapColor);
	}

	public static void registerRecipes() {

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack, @Nullable World worldIn, List<String> infos, ITooltipFlag flagIn) {
		if (itemstack != null) {
			String name = infos.get(0);
			name = MOStringHelper.translateToLocal(EnumDyeColor
					.byDyeDamage(MathHelper.clamp(itemstack.getItemDamage(), 0, ItemDye.DYE_COLORS.length - 1))
					.getTranslationKey() + " " + name);
			infos.set(0, name);
		}
	}

	@Override
	public void registerOreDict() {
		for (int i = 0; i < 16; i++) {
			OreDictionary.registerOre("blockFloorTile",
					new ItemStack(MatterOverdrive.BLOCKS.decorative_floor_tile, 1, i));
			OreDictionary.registerOre("blockFloorTiles",
					new ItemStack(MatterOverdrive.BLOCKS.decorative_floor_tiles, 1, i));
		}
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (EnumDyeColor color : EnumDyeColor.values()) {
			items.add(new ItemStack(this, 1, color.getMetadata()));
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(COLOR).getMetadata();
	}

	@Nonnull
	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, COLOR);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(COLOR).getMetadata();
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return MapColor.getBlockColor(state.getValue(COLOR));
	}

}

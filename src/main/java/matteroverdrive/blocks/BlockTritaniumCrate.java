package matteroverdrive.blocks;

import matteroverdrive.Reference;
import matteroverdrive.api.wrench.IDismantleable;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.data.Inventory;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.tile.TileEntityTritaniumCrate;
import matteroverdrive.util.MOBlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class BlockTritaniumCrate extends MOBlockMachine<TileEntityTritaniumCrate> implements IDismantleable {

	private static final AxisAlignedBB BOX_NORTH_SOUTH = new AxisAlignedBB(0, 0, 2 / 16d, 1, 12 / 16d, 14 / 16d);
	private static final AxisAlignedBB BOX_EAST_WEST = new AxisAlignedBB(2 / 16d, 0, 0, 14 / 16d, 12 / 16d, 1);

	public BlockTritaniumCrate(Material material, String name) {
		super(material, name);
		setHasRotation();
		setHardness(20.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
		setRotationType(MOBlockHelper.RotationType.FOUR_WAY);
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing dir = state.getValue(MOBlock.PROPERTY_DIRECTION);
		return dir == EnumFacing.NORTH || dir == EnumFacing.SOUTH ? BOX_NORTH_SOUTH : BOX_EAST_WEST;
	}

	@Override
	public Class<TileEntityTritaniumCrate> getTileEntityClass() {
		return TileEntityTritaniumCrate.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileEntityTritaniumCrate();
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		}

		ItemStack currentitem = playerIn.getHeldItem(EnumHand.MAIN_HAND);

		if (!currentitem.isEmpty()) {
			// Compare it against the base dye item.
			Item dye = new ItemStack(Items.DYE, 1).getItem();

			if (currentitem.getItem().equals(dye)) {
				playerIn.sendMessage(new TextComponentString("Just clicked with some dye."));

				setRegistryName(new ResourceLocation(Reference.MOD_ID, "tritanium_crate_purple"));
				this.setTranslationKey("tritanium_crate_purple");

				return true;
			}
		}

		TileEntity entity = worldIn.getTileEntity(pos);
		if (entity instanceof TileEntityTritaniumCrate) {
			// FMLNetworkHandler.openGui(entityPlayer, MatterOverdrive.instance,
			// GuiHandler.TRITANIUM_CRATE, world, x, y, z);
			worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), MatterOverdriveSounds.blocksCrateOpen,
					SoundCategory.BLOCKS, 0.5f, 1);
			playerIn.displayGUIChest(((TileEntityTritaniumCrate) entity).getInventory());
			return true;
		}

		return false;
	}

	@Override
	protected Inventory getInventory(World world, BlockPos pos) {
		if (world.getTileEntity(pos) instanceof TileEntityTritaniumCrate) {
			TileEntityTritaniumCrate machine = (TileEntityTritaniumCrate) world.getTileEntity(pos);
			return machine.getInventory();
		}
		return null;
	}

	@Override
	public boolean canDismantle(EntityPlayer player, World world, BlockPos pos) {
		return true;
	}

	@Override
	public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, BlockPos pos, boolean returnDrops) {
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileEntityTritaniumCrate) {
			IBlockState state = world.getBlockState(pos);

			state.getBlock().harvestBlock(world, player, pos, state, world.getTileEntity(pos), ItemStack.EMPTY);

			state.getBlock().removedByPlayer(state, world, pos, player, false);
		}

		return new ArrayList<>();
	}
}
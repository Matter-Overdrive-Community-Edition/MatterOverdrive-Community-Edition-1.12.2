
package matteroverdrive.items;

import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.init.OverdriveFluids;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

public class MatterContainer extends MOBaseItem {

	private final int CONTAINER_CAPACITY = 1000;

	public MatterContainer(String name) {
		super(name);
		setMaxStackSize(8);
	}

	@Override
	public void InitTagCompount(ItemStack stack) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setBoolean("fillMode", true);
		stack.setTagCompound(tagCompound);
	}

	public ItemStack getFullStack() {
		ItemStack full = new ItemStack(this);
		full.getCapability(FLUID_HANDLER_CAPABILITY, null).fill(new FluidStack(OverdriveFluids.matterPlasma, 1000),
				true);
		return full;
	}

	private void sendToPlayer(World world, EntityPlayer player, String str) {
		if (!world.isRemote) {
			player.sendMessage(new TextComponentString(str));
		}
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);

		this.TagCompountCheck(stack);

		NBTTagCompound nbt = stack.getTagCompound();

		if (nbt == null) {
			return EnumActionResult.FAIL;
		}

		if (nbt.getBoolean("fillMode")) {
			return fillContainer(player, world, pos, hand, facing);
		} else {
			return emptyContainer(player, world, pos, hand, facing);
		}
	}

	private EnumActionResult fillContainer(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing facing) {
		// Stack is container, tile is destination.

		ItemStack stack = player.getHeldItem(hand);

		ItemStack workingStack;

		if (stack.getCount() > 1) {
			if (!world.isRemote) {
				player.sendMessage(new TextComponentString(
						"Please only have one container in your hand when attempting to fill or switch modes."));
			}

			return EnumActionResult.FAIL;
		}

		boolean stackSplit = false;

		if (stack.getCount() == 1) {
			workingStack = stack;
		} else {
			workingStack = stack.splitStack(1);

			stackSplit = true;
		}

		TileEntity tile = world.getTileEntity(pos);

		if (tile == null) {
			return EnumActionResult.FAIL;
		}

		if (!stack.hasCapability(FLUID_HANDLER_CAPABILITY, null)) {
			return EnumActionResult.FAIL;
		}

		IFluidHandler container = workingStack.getCapability(FLUID_HANDLER_CAPABILITY, null);

		if (container == null) {
			return EnumActionResult.FAIL;
		}

		if (!tile.hasCapability(FLUID_HANDLER_CAPABILITY, facing)) {
			return EnumActionResult.PASS;
		}

		IFluidHandler handler = tile.getCapability(FLUID_HANDLER_CAPABILITY, facing);

		if (handler == null) {
			return EnumActionResult.PASS;
		}

		IMatterHandler matterStorage = tile.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, facing);

		if (matterStorage == null) {
			return EnumActionResult.PASS;
		}

		FluidStack amountInSource = handler.drain(matterStorage.getCapacity(), false);

		int availableAmount = 0;

		if (amountInSource != null) {
			FluidStack available = handler.drain(amountInSource.amount, false);

			availableAmount = available != null ? available.amount : 0;
		}

		FluidStack amount = stack.getCapability(FLUID_HANDLER_CAPABILITY, null).drain(CONTAINER_CAPACITY, false);

		int hasAmount = amount != null ? amount.amount : 0;

		int freeSpace = 1000 - hasAmount;

		// Only drain the amount that's either the free space in the container, or
		// what's left in the tile entity.
		int drainAmount = Math.min(availableAmount, freeSpace);

		FluidStack result = handler.drain(drainAmount, true);

		container.fill(result, true);

		// This code is bugged.

//        if (stackSplit) {
//            if (!player.addItemStackToInventory(workingStack)) {
//                player.entityDropItem(workingStack, 0.0f);
//            }
//        }

		return EnumActionResult.SUCCESS;
	}

	private EnumActionResult emptyContainer(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing facing) {
		// Stack is container, tile is destination.

		ItemStack stack = player.getHeldItem(hand);
		TileEntity tile = world.getTileEntity(pos);

		if (tile == null) {
			return EnumActionResult.FAIL;
		}

		if (!stack.hasCapability(FLUID_HANDLER_CAPABILITY, null)) {
			return EnumActionResult.FAIL;
		}

		IFluidHandler container = stack.getCapability(FLUID_HANDLER_CAPABILITY, null);

		if (container == null) {
			return EnumActionResult.FAIL;
		}

		FluidStack amountInContainer = container.drain(CONTAINER_CAPACITY, false);

		int amountInContainerInt = amountInContainer != null ? amountInContainer.amount : 0;

		if (!tile.hasCapability(FLUID_HANDLER_CAPABILITY, facing)) {
			return EnumActionResult.PASS;
		}

		IFluidHandler handler = tile.getCapability(FLUID_HANDLER_CAPABILITY, facing);

		if (handler == null) {
			return EnumActionResult.PASS;
		}

		IMatterHandler matterStorage = tile.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, facing);

		if (matterStorage == null) {
			return EnumActionResult.PASS;
		}

		FluidStack amountInDest = handler.drain(matterStorage.getCapacity(), false);

		int amountInDestInt = amountInDest != null ? amountInDest.amount : 0;

		int freeSpace = matterStorage.getCapacity() - amountInDestInt;

		sendToPlayer(world, player, "Amount of space free in destination: " + freeSpace);

		// Only drain the amount that's either the free space in the container, or
		// what's left in the tile entity.
		int drainAmount = Math.min(amountInContainerInt, freeSpace);

		FluidStack result = container.drain(drainAmount, true);

		handler.fill(result, true);

		return EnumActionResult.SUCCESS;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);

		// Don't apply commands to stacks of items.
		if (stack.getCount() > 1) {
			return ActionResult.newResult(EnumActionResult.FAIL, stack);
		}

		this.TagCompountCheck(stack);

		if (hand == EnumHand.OFF_HAND) {
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}

		playerIn.setActiveHand(hand);

		NBTTagCompound nbt = stack.getTagCompound();

		if (nbt == null) {
			return ActionResult.newResult(EnumActionResult.FAIL, stack);
		}

		boolean modeFill = nbt.getBoolean("fillMode");

		modeFill = !modeFill;

		if (modeFill) {
			sendToPlayer(worldIn, playerIn, "Switched to fill mode.");
		} else {
			sendToPlayer(worldIn, playerIn, "Switched to empty mode.");
		}

		nbt.setBoolean("fillMode", modeFill);

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		FluidStack result = stack.getCapability(FLUID_HANDLER_CAPABILITY, null).drain(1000, false);
		return super.getTranslationKey(stack) + (result == null ? "_empty" : "_full");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		FluidStack result = stack.getCapability(FLUID_HANDLER_CAPABILITY, null).drain(1000, false);
		int amount = result == null ? 0 : result.amount;
		tooltip.add("Amount: " + amount);
	}

	@Override
	public void initItemModel() {
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			FluidStack result = stack.getCapability(FLUID_HANDLER_CAPABILITY, null).drain(1000, false);
			int amount = result == null ? 0 : result.amount;
			float percent = amount / 1000f;
			return new ModelResourceLocation(getRegistryName(),
					percent == 1 ? "level=full" : percent > 0 ? "level=partial" : "level=empty");
		});
		ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), "level=full"),
				new ModelResourceLocation(getRegistryName(), "level=partial"),
				new ModelResourceLocation(getRegistryName(), "level=empty"));
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (isInCreativeTab(tab)) {
			subItems.add(new ItemStack(this));
			subItems.add(getFullStack());
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new MatterContainerCapabilityProvider();
	}

	public static class MatterContainerCapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {

		private FluidTank tank = new FluidTank(1000) {
			@Override
			public boolean canFillFluidType(FluidStack fluid) {
				if (fluid == null) {
					return false;
				}

				return fluid.getFluid() == OverdriveFluids.matterPlasma;
			}
		};

		@Override
		public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == FLUID_HANDLER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
			return capability == FLUID_HANDLER_CAPABILITY ? (T) tank : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return tank.writeToNBT(new NBTTagCompound());
		}

		@Override
		public void deserializeNBT(NBTTagCompound tag) {
			tank.readFromNBT(tag);
		}

	}

}

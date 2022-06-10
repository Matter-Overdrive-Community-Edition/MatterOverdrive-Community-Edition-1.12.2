
package matteroverdrive.tile;

import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.BionicSlot;
import matteroverdrive.data.inventory.EnergySlot;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.events.MachineEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class TileEntityAndroidStation extends MOTileEntityMachine {
	public int HEAD_SLOT;
	public int ARMS_SLOT;
	public int LEGS_SLOT;
	public int CHEST_SLOT;
	public int OTHER_SLOT;
	public int BATTERY_SLOT;

	public TileEntityAndroidStation() {
		super(0);
	}

	@Override
	protected void RegisterSlots(Inventory inventory) {
		HEAD_SLOT = inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_HEAD));
		ARMS_SLOT = inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_ARMS));
		LEGS_SLOT = inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_LEGS));
		CHEST_SLOT = inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_CHEST));
		OTHER_SLOT = inventory.AddSlot(new BionicSlot(false, Reference.BIONIC_OTHER));
		BATTERY_SLOT = inventory.AddSlot(new EnergySlot(false));
		super.RegisterSlots(inventory);
	}

	public IInventory getActiveInventory() {
		return inventory;
	}

	@Override
	public SoundEvent getSound() {
		return null;
	}

	@Override
	public boolean hasSound() {
		return false;
	}

	@Override
	public boolean getServerActive() {
		return false;
	}

	@Override
	public float soundVolume() {
		return 0;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot(int slot) {
		return super.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int size) {
		return super.decrStackSize(slot, size);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		super.setInventorySlotContents(slot, itemStack);
	}

	@Override
	public boolean isAffectedByUpgrade(UpgradeTypes type) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos(), getPos().add(1, 3, 1));
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return MOPlayerCapabilityProvider.GetAndroidCapability(player) != null
				&& MOPlayerCapabilityProvider.GetAndroidCapability(player).isAndroid()
				&& super.isUsableByPlayer(player);
	}

	@Override
	protected void onMachineEvent(MachineEvent event) {

	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}
}

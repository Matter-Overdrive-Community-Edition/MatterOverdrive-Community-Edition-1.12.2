
package matteroverdrive.tile;

import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.ItemInventoryWrapper;
import matteroverdrive.data.inventory.ModuleSlot;
import matteroverdrive.data.inventory.WeaponSlot;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class TileEntityWeaponStation extends MOTileEntityMachine {
	public int INPUT_SLOT;
	public int BATTERY_MODULE;
	public int COLOR_MODULE;
	public int BARREL_MODULE;
	public int SIGHTS_MODULE;
	public int OTHER_MODULE_ONE;
	public int OTHER_MODULE_TWO;

	private IInventory itemInventory;

	public TileEntityWeaponStation() {
		super(0);
	}

	@Override
	protected void RegisterSlots(Inventory inventory) {
		WeaponSlot weaponSlot = (WeaponSlot) new WeaponSlot(true).setSendToClient(true);
		BATTERY_MODULE = inventory.AddSlot(new ModuleSlot(false, Reference.MODULE_BATTERY, weaponSlot));
		COLOR_MODULE = inventory.AddSlot(new ModuleSlot(false, Reference.MODULE_COLOR, weaponSlot));
		BARREL_MODULE = inventory.AddSlot(new ModuleSlot(false, Reference.MODULE_BARREL, weaponSlot));
		SIGHTS_MODULE = inventory.AddSlot(new ModuleSlot(false, Reference.MODULE_SIGHTS, weaponSlot));
		OTHER_MODULE_ONE = inventory.AddSlot(new ModuleSlot(false, Reference.MODULE_OTHER, weaponSlot));
		OTHER_MODULE_TWO = inventory.AddSlot(new ModuleSlot(false, Reference.MODULE_OTHER, weaponSlot));
		INPUT_SLOT = inventory.AddSlot(weaponSlot);
		super.RegisterSlots(inventory);
	}

	public IInventory getActiveInventory() {
		if (itemInventory == null && !inventory.getSlot(INPUT_SLOT).getItem().isEmpty()
				&& WeaponHelper.isWeapon(inventory.getSlot(INPUT_SLOT).getItem())) {
			itemInventory = new ItemInventoryWrapper(inventory.getSlot(INPUT_SLOT).getItem(), 6);
		}
		if (inventory.getSlot(INPUT_SLOT).getItem().isEmpty()
				|| !WeaponHelper.isWeapon(inventory.getSlot(INPUT_SLOT).getItem())) {
			itemInventory = null;
		}
		return itemInventory == null ? inventory : itemInventory;
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
		if (slot != INPUT_SLOT && slot < INPUT_SLOT) {
			return getActiveInventory().getStackInSlot(slot);
		} else {
			return super.getStackInSlot(slot);
		}
	}

	public boolean isItemValidForSlot(int slot, ItemStack item) {
		if (slot != INPUT_SLOT && slot < INPUT_SLOT) {
			return getActiveInventory().isItemValidForSlot(slot, item);
		} else {
			return super.isItemValidForSlot(slot, item);
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int size) {
		if (slot != INPUT_SLOT && slot < INPUT_SLOT) {
			return getActiveInventory().decrStackSize(slot, size);
		} else {
			return super.decrStackSize(slot, size);
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (slot != INPUT_SLOT && slot < INPUT_SLOT) {
			getActiveInventory().setInventorySlotContents(slot, itemStack);
		} else {
			super.setInventorySlotContents(slot, itemStack);
		}
	}

	@Override
	public boolean isAffectedByUpgrade(UpgradeTypes type) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX() + 1,
				getPos().getY() + 2, getPos().getZ() + 1);
	}

	@Override
	protected void onMachineEvent(MachineEvent event) {

	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}

}

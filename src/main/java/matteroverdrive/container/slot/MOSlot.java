
package matteroverdrive.container.slot;

import matteroverdrive.client.render.HoloIcon;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MOSlot extends Slot {
	protected String unlocalizedTooltip;
	boolean isVisible = true;

	public MOSlot(IInventory inventory, int slot, int x, int y) {
		super(inventory, slot, x, y);
	}

	@SideOnly(Side.CLIENT)
	public boolean func_111238_b() {
		return isVisible;
	}

	@Override
	public ItemStack getStack() {
		return super.getStack() == null ? ItemStack.EMPTY : super.getStack();
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the
	 * armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return isValid(itemStack);
	}

	public boolean isValid(ItemStack itemStack) {
		return true;
	}

	public void setVisible(boolean visible) {
		this.isVisible = visible;
	}

	@SideOnly(Side.CLIENT)
	public HoloIcon getHoloIcon() {
		return null;
	}

	public String getUnlocalizedTooltip() {
		return unlocalizedTooltip;
	}
}

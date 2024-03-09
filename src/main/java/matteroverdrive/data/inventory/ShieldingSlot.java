
package matteroverdrive.data.inventory;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShieldingSlot extends Slot {
	public ShieldingSlot(boolean isMainSlot) {
		super(isMainSlot);
	}

	@Override
	public boolean isValidForSlot(ItemStack itemStack) {
		if (this.getItem().getCount() < 4) {
			if (itemStack != null && !itemStack.isEmpty()) {
				return itemStack.getItem() == MatterOverdrive.ITEMS.tritanium_plate;
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public HoloIcon getHoloIcon() {
		return ClientProxy.holoIcons.getIcon("shielding");
	}

	@Override
	public int getMaxStackSize() {
		return 5;
	}

	@Override
	public boolean keepOnDismantle() {
		return true;
	}

	@Override
	public String getUnlocalizedTooltip() {
		return "gui.tooltip.slot.shielding";
	}
}

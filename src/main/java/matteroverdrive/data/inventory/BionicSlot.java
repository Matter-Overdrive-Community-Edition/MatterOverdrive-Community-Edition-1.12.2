
package matteroverdrive.data.inventory;

import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BionicSlot extends Slot {
	public static final String[] names = { "head", "arms", "legs", "chest", "other", "battery" };
	private final int type;
	public ResourceLocation[] icons = new ResourceLocation[] {,};

	public BionicSlot(boolean isMainSlot, int type) {
		super(isMainSlot);
		this.type = type;
	}

	@Override
	public boolean isValidForSlot(ItemStack item) {
		return item.getItem() instanceof IBionicPart && ((IBionicPart) item.getItem()).getType(item) == type;
	}

	@Override
	public HoloIcon getHoloIcon() {
		if (type < names.length) {
			return ClientProxy.holoIcons.getIcon("android_slot_" + names[type]);
		}
		return null;
	}

	@Override
	public String getUnlocalizedTooltip() {
		if (type < names.length) {
			return String.format("gui.tooltip.slot.bionic.%s", names[type]);
		}
		return null;
	}
}

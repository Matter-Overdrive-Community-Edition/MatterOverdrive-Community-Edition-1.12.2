
package matteroverdrive.data.inventory;

import matteroverdrive.util.WeaponHelper;
import net.minecraft.item.ItemStack;

public class WeaponSlot extends Slot {
    public WeaponSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack item) {
        return WeaponHelper.isWeapon(item);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public String getUnlocalizedTooltip() {
        return "gui.tooltip.slot.weapon";
    }
}

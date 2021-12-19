
package matteroverdrive.data.inventory;

import net.minecraft.item.ItemStack;

public class RemoveOnlySlot extends Slot {
    public RemoveOnlySlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack itemStack) {
        return false;
    }
}

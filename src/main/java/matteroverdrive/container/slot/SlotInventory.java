
package matteroverdrive.container.slot;

import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.data.inventory.Slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SlotInventory extends MOSlot {

    Slot slot;

    public SlotInventory(IInventory inventory, Slot slot, int x, int y) {
        super(inventory, slot.getId(), x, y);
        this.slot = slot;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return slot.isValidForSlot(itemStack);
    }

    public int getSlotStackLimit() {
        return slot.getMaxStackSize();
    }

    public Slot getSlot() {
        return slot;
    }

    public String getUnlocalizedTooltip() {
        return slot.getUnlocalizedTooltip();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public HoloIcon getHoloIcon() {
        return slot.getHoloIcon();
    }

    public void onSlotChanged() {
        super.onSlotChanged();
        slot.onSlotChanged();
    }
}

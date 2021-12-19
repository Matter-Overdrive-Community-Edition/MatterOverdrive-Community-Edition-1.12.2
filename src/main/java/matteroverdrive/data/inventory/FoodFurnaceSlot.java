package matteroverdrive.data.inventory;

import matteroverdrive.api.matter.IRecyclable;
import matteroverdrive.items.food.MOItemFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class FoodFurnaceSlot extends Slot {
    public FoodFurnaceSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    public boolean isValidForSlot(ItemStack item) {
        return item.getItem() instanceof ItemFood;
    }

    @Override
    public String getUnlocalizedTooltip() {
        return "gui.tooltip.slot.foodfurnace";
    }
}

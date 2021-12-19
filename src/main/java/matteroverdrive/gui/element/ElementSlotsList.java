
package matteroverdrive.gui.element;

import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.data.Inventory;
import matteroverdrive.gui.MOGuiBase;

public class ElementSlotsList extends ElementBaseGroup {
    ElementInventorySlot mainSlot;
    int margin = 0;

    public ElementSlotsList(MOGuiBase gui, int posX, int posY, int width, int height, Inventory inventory, int main) {
        super(gui, posX, posY, width, height);
        int index = 0;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (inventory.getSlot(i).isMainSlot()) {
                if (index == main) {
                    mainSlot = new ElementInventorySlot(gui, (MOSlot) gui.inventorySlots.getSlot(i), 0, 0, 37, 22, "big_main", inventory.getSlot(i).getHoloIcon());
                    mainSlot.setItemOffset(3, 3);
                    addElement(mainSlot);
                } else {

                    addElement(new ElementInventorySlot(gui, (MOSlot) gui.inventorySlots.getSlot(i), 0, 0, 22, 22, "big", inventory.getSlot(i).getHoloIcon()));
                }

                index++;
            }
        }
    }

    @Override
    public void update(int mouseX, int mouseY, float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);

        int height = 0;
        for (MOElementBase element : elements) {
            element.setPosition(0, height);
            height += element.getHeight() + margin;
        }
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public ElementSlot getMainSlot() {
        return mainSlot;
    }
}

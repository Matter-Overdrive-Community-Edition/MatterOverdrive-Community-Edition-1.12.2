
package matteroverdrive.container;

import matteroverdrive.tile.TileEntityMachineSolarPanel;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerSolarPanel extends ContainerMachine<TileEntityMachineSolarPanel> {
    private int lastChargeAmount;

    public ContainerSolarPanel(InventoryPlayer inventory, TileEntityMachineSolarPanel machine) {
        super(inventory, machine);
    }

    @Override
    public void init(InventoryPlayer inventory) {
        addAllSlotsFromInventory(machine.getInventoryContainer());
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, true, true);
    }

    @Override
    public void addListener(IContainerListener icrafting) {
        super.addListener(icrafting);
        icrafting.sendWindowProperty(this, 1, this.machine.getChargeAmount());
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : this.listeners) {
            if (this.lastChargeAmount != this.machine.getChargeAmount()) {
                listener.sendWindowProperty(this, 1, this.machine.getChargeAmount());
            }
        }

        this.lastChargeAmount = this.machine.getChargeAmount();
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int newValue) {
        super.updateProgressBar(slot, newValue);
        if (slot == 1) {
            this.machine.setChargeAmount((byte) newValue);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}

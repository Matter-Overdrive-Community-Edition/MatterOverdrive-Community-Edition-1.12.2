
package matteroverdrive.gui;

import matteroverdrive.Reference;
import matteroverdrive.container.ContainerFactory;
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.gui.element.*;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.machines.decomposer.TileEntityMachineDecomposer;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiDecomposer extends MOGuiMachine<TileEntityMachineDecomposer> {
    MOElementEnergy energyElement;
    ElementMatterStored matterElement;
    ElementDualScaled decompose_progress;
    ElementSlot outputSlot;

    public GuiDecomposer(InventoryPlayer inventoryPlayer, TileEntityMachineDecomposer entity) {
        super(ContainerFactory.createMachineContainer(entity, inventoryPlayer), entity);
        name = "decomposer";
        matterElement = new ElementMatterStored(this, 74, 39, machine.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null));
        energyElement = new MOElementEnergy(this, 100, 39, machine.getEnergyStorage());
        decompose_progress = new ElementDualScaled(this, 32, 55);
        outputSlot = new ElementInventorySlot(this, getContainer().getSlotAt(machine.OUTPUT_SLOT_ID), 129, 55, 22, 22, "big");

        decompose_progress.setMode(1);
        decompose_progress.setSize(24, 16);
        decompose_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
    }

    @Override
    public void initGui() {
        super.initGui();

        pages.get(0).addElement(outputSlot);
        pages.get(0).addElement(energyElement);
        pages.get(0).addElement(matterElement);
        this.addElement(decompose_progress);

        AddMainPlayerSlots(this.inventorySlots, pages.get(0));
        AddHotbarPlayerSlots(this.inventorySlots, this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
                                                   int p_146976_2_, int p_146976_3_) {
        super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
        decompose_progress.setQuantity(Math.round((((ContainerMachine) getContainer()).getProgress() * 24)));
        ManageReqiremnetsTooltips();
    }

    void ManageReqiremnetsTooltips() {
        if (machine.getStackInSlot(machine.INPUT_SLOT_ID) != null) {
            int matterAmount = MatterHelper.getMatterAmountFromItem(machine.getStackInSlot(machine.INPUT_SLOT_ID));
            energyElement.setEnergyRequired(-(machine.getEnergyDrainMax()));
            matterElement.setDrain(matterAmount);
            energyElement.setEnergyRequiredPerTick(-machine.getEnergyDrainPerTick());
        } else {
            energyElement.setEnergyRequired(0);
            matterElement.setDrain(0);
            energyElement.setEnergyRequiredPerTick(0);
        }
    }
}

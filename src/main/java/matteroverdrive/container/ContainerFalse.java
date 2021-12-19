
package matteroverdrive.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerFalse extends MOBaseContainer {

    public ContainerFalse() {
        super(null);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }

    @Override
    public void putStackInSlot(int p_75141_1_, ItemStack p_75141_2_) {

    }
}

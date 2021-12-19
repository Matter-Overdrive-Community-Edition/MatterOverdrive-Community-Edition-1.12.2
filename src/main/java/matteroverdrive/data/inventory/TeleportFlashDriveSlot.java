
package matteroverdrive.data.inventory;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TeleportFlashDriveSlot extends Slot {
    public TeleportFlashDriveSlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    public boolean isValidForSlot(ItemStack item) {
        return item != null && item.getItem() == MatterOverdrive.ITEMS.transportFlashDrive;
    }

    @SideOnly(Side.CLIENT)
    public HoloIcon getHoloIcon() {
        return ClientProxy.holoIcons.getIcon("flash_drive");
    }

    @Override
    public String getUnlocalizedTooltip() {
        return "item.transport_flash_drive.name";
    }
}

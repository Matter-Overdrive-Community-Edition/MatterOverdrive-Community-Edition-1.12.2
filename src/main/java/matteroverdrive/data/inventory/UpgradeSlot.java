
package matteroverdrive.data.inventory;

import matteroverdrive.api.IUpgradeable;
import matteroverdrive.api.inventory.IUpgrade;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public class UpgradeSlot extends Slot {
    private IUpgradeable upgradeable;

    public UpgradeSlot(boolean isMainSlot, IUpgradeable upgradeable) {
        super(isMainSlot);
        this.upgradeable = upgradeable;
    }

    @Override
    public boolean isValidForSlot(ItemStack item) {
        if (item.getItem() instanceof IUpgrade) {
            IUpgrade upgrade = (IUpgrade) item.getItem();
            UpgradeTypes mainUpgradeType = upgrade.getMainUpgrade(item);
            if (mainUpgradeType != null) {
                return upgradeable.isAffectedByUpgrade(mainUpgradeType);
            } else {
                Map<UpgradeTypes, Double> upgradeMap = upgrade.getUpgrades(item);
                for (final Map.Entry<UpgradeTypes, Double> entry : upgradeMap.entrySet()) {
                    if (upgradeable.isAffectedByUpgrade(entry.getKey())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public HoloIcon getHoloIcon() {
        return ClientProxy.holoIcons.getIcon("upgrade");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean keepOnDismantle() {
        return true;
    }

    @Override
    public String getUnlocalizedTooltip() {
        return "gui.tooltip.slot.upgrade";
    }
}

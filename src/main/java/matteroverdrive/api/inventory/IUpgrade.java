
package matteroverdrive.api.inventory;

import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * Created by Simeon on 4/11/2015.
 * This is used by Machines to increase stats.
 */
public interface IUpgrade {
    /**
     * A map of all the Upgrade Stats the Upgrade changes.
     *
     * @param itemStack The Upgrade Item Stack.
     * @return A map fo Upgrade Types.
     */
    Map<UpgradeTypes, Double> getUpgrades(ItemStack itemStack);

    /**
     * Return the main Upgrade type for the given Upgrade.
     * This is to check if an upgrade can even go in the upgrade slot.
     *
     * @param itemStack the upgrade stack.
     * @return the main upgrade type
     */
    UpgradeTypes getMainUpgrade(ItemStack itemStack);
}

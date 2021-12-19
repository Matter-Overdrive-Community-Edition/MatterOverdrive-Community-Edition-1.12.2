
package matteroverdrive.api.weapon;

import net.minecraft.item.ItemStack;

public interface IWeaponScope extends IWeaponModule {
    float getZoomAmount(ItemStack scopeStack, ItemStack weaponStack);

    float getAccuracyModify(ItemStack scopeStack, ItemStack weaponStack, boolean zoomed, float originalAccuracy);
}

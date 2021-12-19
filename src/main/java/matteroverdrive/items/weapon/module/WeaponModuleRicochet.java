
package matteroverdrive.items.weapon.module;

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.WeaponStats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class WeaponModuleRicochet extends WeaponModuleBase {
    public WeaponModuleRicochet(String name) {
        super(name);
        applySlot(Reference.MODULE_OTHER);
        applyWeaponStat(0, WeaponStats.RICOCHET, 1);
    }

    @Override
    public String getModelPath() {
        return null;
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack module) {
        return null;
    }

    @Override
    public String getModelName(ItemStack module) {
        return null;
    }
}

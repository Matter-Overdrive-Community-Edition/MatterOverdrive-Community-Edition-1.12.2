
package matteroverdrive.items.weapon.module;

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponScope;
import matteroverdrive.api.weapon.WeaponStats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class WeaponModuleSniperScope extends WeaponModuleBase implements IWeaponScope {
    public WeaponModuleSniperScope(String name) {
        super(name);
        setHasSubtypes(false);
        applySlot(Reference.MODULE_SIGHTS);
        applyWeaponStat(0, WeaponStats.RANGE, 1.5f);
    }

    @Override
    public float getZoomAmount(ItemStack scopeStack, ItemStack weapon) {
        return 0.85f;
    }

    @Override
    public float getAccuracyModify(ItemStack scopeStack, ItemStack weaponStack, boolean zoomed, float originalAccuracy) {
        if (zoomed) {
            return originalAccuracy * 0.4f;
        }
        return originalAccuracy + 4f;
    }

    @Override
    public String getModelPath() {
        return Reference.PATH_MODEL_ITEMS + "sniper_scope.obj";
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack module) {
        return new ResourceLocation(Reference.PATH_ITEM + "sniper_scope_texture.png");
    }

    @Override
    public String getModelName(ItemStack module) {
        return "sniper_scope";
    }
}

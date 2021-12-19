package matteroverdrive.items.weapon.module;

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponScope;
import matteroverdrive.items.IAdvancedModelProvider;
import matteroverdrive.util.MOLog;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class WeaponModuleHoloSights extends WeaponModuleBase implements IWeaponScope, IAdvancedModelProvider {
    public static final String[] subItemNames = {"normal", "wide", "small"};

    public WeaponModuleHoloSights(String name) {
        super(name);
        applySlot(Reference.MODULE_SIGHTS);
        this.setHasSubtypes(true);
    }

    @Override
    public String[] getSubNames() {
        return subItemNames;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs creativeTabs, @Nonnull NonNullList<ItemStack> list) {
        if (!isInCreativeTab(creativeTabs)) {
            return;
        }

        for (int i=0; i<subItemNames.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack stack) {
        int i = MathHelper.clamp(stack.getItemDamage(), 0, 3);
        return super.getTranslationKey() + "." + subItemNames[i];
    }

    // This fixed the holo sights issue.
    @Override
    public String getModelPath() {
        return Reference.PATH_MODEL_ITEMS + "weapon_model_holo_sights.obj";
    };

    @Override
    public ResourceLocation getModelTexture(ItemStack module) {
        ResourceLocation resource = new ResourceLocation(Reference.PATH_ELEMENTS + String.format("holo_sight_%d.png", module.getItemDamage()));

        MOLog.warn("Resource location is: " + resource.toString());

        return resource;
    }

    @Override
    public String getModelName(ItemStack module) {
        int i = MathHelper.clamp(module.getItemDamage(), 0, subItemNames.length - 1);

        String mName = super.getTranslationKey() + "_" + subItemNames[i];

        MOLog.info("The current model name is: {}", mName);

        return super.getTranslationKey() + "_" + subItemNames[i];
    }

    @Override
    public float getZoomAmount(ItemStack scopeStack, ItemStack weaponStack) {
        return 0.3f;
    }

    @Override
    public float getAccuracyModify(ItemStack scopeStack, ItemStack weaponStack, boolean zoomed, float originalAccuracy) {
        if (zoomed) {
            return originalAccuracy * 0.6f;
        }
        return originalAccuracy * 0.8f;
    }
}

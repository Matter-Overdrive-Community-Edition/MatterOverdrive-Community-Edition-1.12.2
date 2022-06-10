
package matteroverdrive.items.weapon.module;

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponScope;
import matteroverdrive.items.IAdvancedModelProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class WeaponModuleHoloSightsOld extends WeaponModuleBase implements IWeaponScope, IAdvancedModelProvider {
	public static final String[] subItemNames = { "normal", "wide", "small" };

	public WeaponModuleHoloSightsOld(String name) {
		super(name);
		applySlot(Reference.MODULE_SIGHTS);
		this.setHasSubtypes(true);
	}

	@Override
	public String[] getSubNames() {
		return subItemNames;
	}

//    @SideOnly(Side.CLIENT)
//    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
//        if (isInCreativeTab(tab)) {
//            for (int i = 0; i < 3; i++) {
//                subItems.add(new ItemStack(itemIn, 1, i));
//            }
//        }
//    }

	public void getSubItems(CreativeTabs tab, List<ItemStack> subItems) {
		if (isInCreativeTab(tab)) {
			for (int i = 0; i < subItemNames.length; i++) {
				subItems.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	public float getZoomAmount(ItemStack scopeStack, ItemStack weaponStack) {
		return 0.3f;
	}

	@Override
	public float getAccuracyModify(ItemStack scopeStack, ItemStack weaponStack, boolean zoomed,
			float originalAccuracy) {
		if (zoomed) {
			return originalAccuracy * 0.6f;
		}
		return originalAccuracy * 0.8f;
	}

	@Override
	public String getModelPath() {
		return null;
	}

	@Override
	public ResourceLocation getModelTexture(ItemStack module) {
//        System.out.println("Getting texture for model texture with item damage of: " + module.getItemDamage());
//
//        return new ResourceLocation(Reference.PATH_ELEMENTS + String.format("holo_sight_%s.png", module.getItemDamage()));

		return null;
	}

	@Override
	public String getModelName(ItemStack module) {
		return null;
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack
	 * so different stacks can have different names based on their damage or NBT.
	 */
	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		int i = MathHelper.clamp(stack.getItemDamage(), 0, subItemNames.length - 1);

		return super.getTranslationKey() + "." + subItemNames[i];
	}
}

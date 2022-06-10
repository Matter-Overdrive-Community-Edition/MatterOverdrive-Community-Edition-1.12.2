
package matteroverdrive.util;

import java.util.List;

import javax.annotation.Nonnull;

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.api.weapon.IWeaponColor;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.api.weapon.IWeaponStat;
import matteroverdrive.items.weapon.module.WeaponModuleColor;
import net.minecraft.item.ItemStack;

public class WeaponHelper {
	@Nonnull
	public static ItemStack getModuleAtSlot(int slot, ItemStack weapon) {
		if (isWeapon(weapon)) {
			return MOInventoryHelper.getStackInSlot(weapon, slot);
		}
		return ItemStack.EMPTY;
	}

	public static boolean hasModule(int module, ItemStack weapon) {
		return !MOInventoryHelper.getStackInSlot(weapon, module).isEmpty();
	}

	public static void setModuleAtSlot(int slot, ItemStack weapon, ItemStack module) {
		if (isWeapon(weapon) && module != null) {
			MOInventoryHelper.setInventorySlotContents(weapon, slot, module);
		}
	}

	public static int getColor(ItemStack weapon) {
		ItemStack module = getModuleAtSlot(Reference.MODULE_COLOR, weapon);
		if (!module.isEmpty() && isWeaponModule(module)) {
			return ((IWeaponColor) module.getItem()).getColor(module, weapon);
		}
		return WeaponModuleColor.defaultColor.getColor();
	}

	public static float modifyStat(IWeaponStat stat, ItemStack weapon, float original) {
		if (isWeapon(weapon)) {
			List<ItemStack> itemStacks = MOInventoryHelper.getStacks(weapon);
			for (ItemStack module : itemStacks) {
				if (module != null && module.getItem() instanceof IWeaponModule) {
					original += ((IWeaponModule) module.getItem()).modifyWeaponStat(stat, module, weapon, original);
				}
			}
		}
		return original;
	}

	public static boolean hasStat(IWeaponStat stat, ItemStack weapon) {
		float statValue = 1f;
		if (isWeapon(weapon)) {
			for (ItemStack module : MOInventoryHelper.getStacks(weapon)) {
				if (module != null && module.getItem() instanceof IWeaponModule) {
					statValue = ((IWeaponModule) module.getItem()).modifyWeaponStat(stat, module, weapon, statValue);
				}
			}
		}
		return statValue != 1f;
	}

	public static boolean isWeaponModule(ItemStack itemStack) {
		return (!itemStack.isEmpty() && itemStack.getItem() instanceof IWeaponModule);
	}

	public static boolean isWeapon(ItemStack itemStack) {
		return !itemStack.isEmpty() && itemStack.getItem() instanceof IWeapon;
	}
}

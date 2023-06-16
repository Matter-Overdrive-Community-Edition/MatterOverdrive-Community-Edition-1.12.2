
package matteroverdrive.items.weapon.module;

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.WeaponStats;
import matteroverdrive.items.IAdvancedModelProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WeaponModuleBarrel extends WeaponModuleBase implements IAdvancedModelProvider {
	public static final int DAMAGE_BARREL_ID = 0;
	public static final int FIRE_BARREL_ID = 1;
	public static final int EXPLOSION_BARREL_ID = 2;
	public static final int HEAL_BARREL_ID = 3;
	public static final int DOOMSDAY_BARREL_ID = 4;
	public static final int BLOCK_BARREL_ID = 5;
	public static final String[] names = { "damage", "fire", "explosion", "heal", "doomsday", "block" };

	public WeaponModuleBarrel(String name) {
		super(name);
		applySlot(Reference.MODULE_BARREL);
		applyWeaponStat(DAMAGE_BARREL_ID, WeaponStats.DAMAGE, 1.5f);
		applyWeaponStat(DAMAGE_BARREL_ID, WeaponStats.AMMO, 0.5f);
		applyWeaponStat(DAMAGE_BARREL_ID, WeaponStats.EFFECT, 0.5f);

		applyWeaponStat(FIRE_BARREL_ID, WeaponStats.DAMAGE, 0.5f);
		applyWeaponStat(FIRE_BARREL_ID, WeaponStats.FIRE_DAMAGE, 10);
		applyWeaponStat(FIRE_BARREL_ID, WeaponStats.AMMO, 0.5f);

		applyWeaponStat(EXPLOSION_BARREL_ID, WeaponStats.EXPLOSION_DAMAGE, 1);
		applyWeaponStat(EXPLOSION_BARREL_ID, WeaponStats.AMMO, 0.2);
		applyWeaponStat(EXPLOSION_BARREL_ID, WeaponStats.EFFECT, 0.5);
		applyWeaponStat(EXPLOSION_BARREL_ID, WeaponStats.FIRE_RATE, 0.15);

		applyWeaponStat(HEAL_BARREL_ID, WeaponStats.DAMAGE, 0);
		applyWeaponStat(HEAL_BARREL_ID, WeaponStats.AMMO, 0.5);
		applyWeaponStat(HEAL_BARREL_ID, WeaponStats.HEAL, 0.1);

		applyWeaponStat(DOOMSDAY_BARREL_ID, WeaponStats.EXPLOSION_DAMAGE, 3f);
		applyWeaponStat(DOOMSDAY_BARREL_ID, WeaponStats.AMMO, 0.2f);
		applyWeaponStat(DOOMSDAY_BARREL_ID, WeaponStats.EFFECT, 0.3f);
		applyWeaponStat(DOOMSDAY_BARREL_ID, WeaponStats.FIRE_RATE, 0.1f);

		applyWeaponStat(BLOCK_BARREL_ID, WeaponStats.DAMAGE, 0f);
		applyWeaponStat(BLOCK_BARREL_ID, WeaponStats.AMMO, 0.5f);
		applyWeaponStat(BLOCK_BARREL_ID, WeaponStats.BLOCK_DAMAGE, 3f);
	}

	@Override
	public String[] getSubNames() {
		return names;
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

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
		if (isInCreativeTab(creativeTabs))
			for (int i = 0; i < names.length; i++) {
				list.add(new ItemStack(this, 1, i));
			}
	}

	@Override
	public String getTranslationKey(ItemStack itemStack) {
		int damage = itemStack.getItemDamage();
		return this.getTranslationKey() + "." + names[damage];
	}
}

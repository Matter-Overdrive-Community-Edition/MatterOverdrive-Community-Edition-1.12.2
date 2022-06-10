
package matteroverdrive.api.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public interface IRangedEnergyWeaponAttackMob {
	ItemStack getWeapon();

	void attackEntityWithRangedAttack(EntityLivingBase target, Vec3d lastSeenPosition, boolean canSee);
}

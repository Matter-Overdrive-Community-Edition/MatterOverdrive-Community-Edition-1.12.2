
package matteroverdrive.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;

public class EntityAIAndroidAttackOnCollide extends EntityAIAttackMelee {
	public EntityAIAndroidAttackOnCollide(EntityCreature creature, double speed, boolean useLongMemory) {
		super(creature, speed, useLongMemory);
	}

	@Override
	public void resetTask() {

	}
}

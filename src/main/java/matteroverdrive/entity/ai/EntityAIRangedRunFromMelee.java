
package matteroverdrive.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAIRangedRunFromMelee extends EntityAIBase {
	Vec3d destinaton;
	private double minDistanceSq;
	private final EntityCreature entity;
	private double moveSpeed;

	public EntityAIRangedRunFromMelee(EntityCreature entity, double moveSpeed) {
		this.entity = entity;
		this.moveSpeed = moveSpeed;
		// setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.getAttackTarget() != null && this.entity.getNavigator().noPath()) {
			double sqDistanceToTargetSq = this.entity.getDistanceSq(this.entity.getAttackTarget());
			if (sqDistanceToTargetSq + 4 < minDistanceSq) {
				int distanceToRun = (int) Math.sqrt(minDistanceSq - sqDistanceToTargetSq);
				destinaton = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, distanceToRun, 4,
						new Vec3d(this.entity.getAttackTarget().posX, this.entity.getAttackTarget().posY,
								this.entity.getAttackTarget().posZ));
				return destinaton != null;
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		if (destinaton != null) {
			this.entity.getNavigator().tryMoveToXYZ(destinaton.x, destinaton.y, destinaton.z, moveSpeed);
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !entity.getNavigator().noPath();
	}

	public void setMinDistance(double minDistance) {
		this.minDistanceSq = minDistance * minDistance;
	}

	public void setMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
}

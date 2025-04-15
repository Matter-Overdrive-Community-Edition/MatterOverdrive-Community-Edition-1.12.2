
package matteroverdrive.entity.ai;

import matteroverdrive.api.entity.IPathableMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAIMoveAlongPath extends EntityAIBase {
	private final IPathableMob<?> pathableMob;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private final double movementSpeed;

	public EntityAIMoveAlongPath(IPathableMob<?> pathableMob, double moveSpeedMultiply) {
		this.pathableMob = pathableMob;
		this.movementSpeed = moveSpeedMultiply;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (pathableMob.getEntity().getAttackTarget() != null) {
			return false;
		} else if (pathableMob.getCurrentTarget() != null) {
			if (!pathableMob.getEntity().getNavigator().noPath())
				return true;
			if (pathableMob.isNearTarget(pathableMob.getCurrentTarget())) {
				pathableMob.onTargetReached(pathableMob.getCurrentTarget());
			} else {
				if (!pathableMob.getEntity().getNavigator().tryMoveToXYZ(pathableMob.getCurrentTarget().x,
						pathableMob.getCurrentTarget().y, pathableMob.getCurrentTarget().z, this.movementSpeed)) {
					Vec3d vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(pathableMob.getEntity(), 8, 2,
							pathableMob.getCurrentTarget());

					if (vec3 == null) {
						return false;
					} else {
						this.movePosX = vec3.x;
						this.movePosY = vec3.y;
						this.movePosZ = vec3.z;
						pathableMob.getEntity().getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ,
								this.movementSpeed);
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return !pathableMob.getEntity().getNavigator().noPath();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {

	}
}

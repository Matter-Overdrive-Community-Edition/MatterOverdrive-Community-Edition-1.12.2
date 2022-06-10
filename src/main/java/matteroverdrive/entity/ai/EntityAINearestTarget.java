
package matteroverdrive.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class EntityAINearestTarget<T extends EntityLivingBase> extends EntityAITarget {
	private final Class targetClass;
	private final int targetChance;
	private final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
	private final Predicate<T> targetEntitySelector;
	private EntityLivingBase targetEntity;

	public EntityAINearestTarget(EntityCreature entity, Class targetClass, int targetChance, boolean shouldCheckSight,
			boolean nearbyOnly, final Predicate<T> predicate) {
		super(entity, shouldCheckSight, nearbyOnly);
		this.targetClass = targetClass;
		this.targetChance = targetChance;
		this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(entity);
		this.setMutexBits(1);
		this.targetEntitySelector = new Predicate<T>() {
			/**
			 * Return whether the specified entity is applicable to this filter.
			 */
			public boolean apply(@Nullable T entity) {
				return !(entity instanceof EntityLivingBase) ? false
						: (entity != null && !predicate.apply(entity) ? false
								: EntityAINearestTarget.this.isSuitableTarget(entity, false));
			}
		};
	}

	public boolean shouldExecute() {
		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
			return false;
		} else {
			double d0 = this.getTargetDistance();
			List list = this.taskOwner.world.getEntitiesWithinAABB(this.targetClass,
					this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), this.targetEntitySelector);
			Collections.sort(list, this.theNearestAttackableTargetSorter);

			if (list.isEmpty()) {
				return false;
			} else {
				this.targetEntity = (EntityLivingBase) list.get(0);
				return true;
			}
		}
	}

	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.targetEntity);
		super.startExecuting();
	}
}

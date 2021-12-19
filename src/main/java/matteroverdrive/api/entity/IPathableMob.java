
package matteroverdrive.api.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.Vec3d;

public interface IPathableMob<T extends EntityCreature> {
    Vec3d getCurrentTarget();

    void onTargetReached(Vec3d pos);

    boolean isNearTarget(Vec3d pos);

    T getEntity();
}

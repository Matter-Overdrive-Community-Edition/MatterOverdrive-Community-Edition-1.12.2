
package matteroverdrive.client.render.conversation;

import matteroverdrive.util.MOPhysicsHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class DialogShotFromBehind extends DialogShot {
    private final float distance;
    private final float sideOffset;

    public DialogShotFromBehind(float distance, float sideOffset) {
        this.distance = distance;
        this.sideOffset = sideOffset;
    }

    @Override
    public boolean positionCamera(EntityLivingBase active, EntityLivingBase other, float ticks, EntityRendererConversation rendererConversation) {
        Vec3d look = rendererConversation.getLook(other, active, ticks);
        double lookDistance = look.length();
        look = new Vec3d(look.x, 0, look.z);
        look = look.normalize();
        Vec3d left = look.crossProduct(new Vec3d(0, 1, 0));
        Vec3d pos = rendererConversation.getPosition(other, ticks).add((left.x * sideOffset) / lookDistance, (left.y * sideOffset) / lookDistance, (left.z * sideOffset) / lookDistance);
        RayTraceResult position = MOPhysicsHelper.rayTrace(pos, other.world, distance, ticks, null, true, false, look, other);
        if (position != null) {
            pos = position.hitVec;
        } else {
            pos.add(look.x * distance, look.y * distance, look.z * distance);
        }
        rendererConversation.setCameraPosition(pos);
        Vec3d rotationLook = pos.subtract(rendererConversation.getPosition(active, ticks)).normalize();
        rendererConversation.rotateCameraYawTo(rotationLook, -90);
        rendererConversation.setCameraPitch(0);
        return true;
    }
}


package matteroverdrive.client.render.conversation;

import matteroverdrive.util.MOPhysicsHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class DialogShotWide extends DialogShot {
	private final float distance;
	private final float heightOffset;
	private final boolean oppositeSide;

	public DialogShotWide(float heightOffset, boolean oppositeSide, float distance) {
		this.heightOffset = heightOffset;
		this.oppositeSide = oppositeSide;
		this.distance = distance;
	}

	@Override
	public boolean positionCamera(EntityLivingBase active, EntityLivingBase other, float ticks,
			EntityRendererConversation rendererConversation) {
		Vec3d centerDir = rendererConversation.getPosition(other, ticks).add(0, heightOffset, 0)
				.subtract(rendererConversation.getPosition(active, ticks).add(0, heightOffset, 0));
		double distance = centerDir.length() / 2 * this.distance;
		Vec3d center = rendererConversation.getPosition(active, ticks).add(centerDir.x / 2, centerDir.y / 2,
				centerDir.z / 2);
		Vec3d centerCross = centerDir.normalize().crossProduct(new Vec3d(0, oppositeSide ? -1 : 1, 0)).normalize();
		RayTraceResult hit = MOPhysicsHelper.rayTraceForBlocks(center, active.world, distance, ticks, null, true, true,
				centerCross);
		Vec3d pos = center.add(centerCross.x * distance, centerCross.y * distance, centerCross.z * distance);
		if (hit != null) {
			pos = hit.hitVec;
		}

		rendererConversation.setCameraPosition(pos.x, pos.y, pos.z);
		rendererConversation.rotateCameraYawTo(centerCross, 90);
		return true;
	}
}


package matteroverdrive.api.renderer;

import matteroverdrive.client.render.conversation.EntityRendererConversation;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by Simeon on 8/9/2015. This is used by the conversation system to
 * transform the "camera" entity.
 */
public interface IDialogShot {
	/**
	 * Used to position the camera entity. Called by the currently active shot for
	 * the conversation, before the camera's world render.
	 *
	 * @param active               the Active entity in the conversation. It may be
	 *                             the conversation NPC or the player.
	 * @param other                the other entity in the conversation. This the
	 *                             the opposite of the active entity. It may be the
	 *                             conversation NPC or the player.
	 * @param ticks                the partial render ticks.
	 * @param rendererConversation the entity camera. This is what needs to be
	 *                             transformed to achieve the desired camera shot.
	 * @return is the camera shot possible.
	 */
	boolean positionCamera(EntityLivingBase active, EntityLivingBase other, float ticks,
			EntityRendererConversation rendererConversation);
}


package matteroverdrive.entity.tasks;

import matteroverdrive.api.dialog.IDialogNpc;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITalkToPlayer extends EntityAIBase {
	private final IDialogNpc npc;

	public EntityAITalkToPlayer(IDialogNpc npc) {
		this.npc = npc;
		this.setMutexBits(5);
	}

	@Override
	public boolean shouldExecute() {
		if (!this.npc.getEntity().isEntityAlive()) {
			return false;
		} else {
			EntityPlayer entityplayer = this.npc.getDialogPlayer();
			return entityplayer != null && (!(this.npc.getEntity().getDistanceSq(entityplayer) > 32.0D)
					&& entityplayer.openContainer != null);
		}
	}

	@Override
	public void startExecuting() {
		this.npc.getEntity().getNavigator().clearPath();
	}

	public void resetTask() {
		this.npc.setDialogPlayer(null);
	}
}

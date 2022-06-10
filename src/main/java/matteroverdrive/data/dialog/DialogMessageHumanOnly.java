
package matteroverdrive.data.dialog;

import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import net.minecraft.entity.player.EntityPlayer;

public class DialogMessageHumanOnly extends DialogMessage {
	public DialogMessageHumanOnly() {
		super();
	}

	public DialogMessageHumanOnly(String message) {
		super(message);
	}

	public DialogMessageHumanOnly(String message, String question) {
		super(message, question);
	}

	@Override
	public boolean isVisible(IDialogNpc npc, EntityPlayer player) {
		return MOPlayerCapabilityProvider.GetAndroidCapability(player) == null
				|| !MOPlayerCapabilityProvider.GetAndroidCapability(player).isAndroid();
	}
}

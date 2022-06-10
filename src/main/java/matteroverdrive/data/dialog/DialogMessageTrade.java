
package matteroverdrive.data.dialog;

import com.google.gson.JsonObject;
import matteroverdrive.api.dialog.IDialogNpc;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;

public class DialogMessageTrade extends DialogMessage {
	public DialogMessageTrade(JsonObject object) {
		super(object);
	}

	public DialogMessageTrade() {
	}

	public DialogMessageTrade(String message) {
		super(message);
	}

	public DialogMessageTrade(String message, String question) {
		super(message, question);
	}

	public DialogMessageTrade(String[] messages, String[] questions) {
		super(messages, questions);
	}

	@Override
	public void onInteract(IDialogNpc npc, EntityPlayer player) {
		if (!player.world.isRemote && npc.getEntity() instanceof IMerchant) {
			((IMerchant) npc.getEntity()).setCustomer(player);
			player.displayVillagerTradeGui((IMerchant) npc);
		}
	}
}


package matteroverdrive.data.dialog;

import com.google.gson.JsonObject;
import matteroverdrive.api.dialog.IDialogNpc;
import net.minecraft.entity.player.EntityPlayer;

public class DialogMessageQuit extends DialogMessage {
	public DialogMessageQuit(JsonObject object) {
		super(object);
	}

	public DialogMessageQuit() {
	}

	public DialogMessageQuit(String message) {
		super(message);
	}

	public DialogMessageQuit(String message, String question) {
		super(message, question);
	}

	public DialogMessageQuit(String[] messages, String[] questions) {
		super(messages, questions);
	}

	@Override
	public void onInteract(IDialogNpc npc, EntityPlayer player) {
		player.closeScreen();
	}

	@Override
	public boolean canInteract(IDialogNpc npc, EntityPlayer player) {
		return true;
	}
}

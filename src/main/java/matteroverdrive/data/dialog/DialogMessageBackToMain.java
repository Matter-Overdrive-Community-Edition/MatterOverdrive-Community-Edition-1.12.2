
package matteroverdrive.data.dialog;

import com.google.gson.JsonObject;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.gui.GuiDialog;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DialogMessageBackToMain extends DialogMessageBack {
	public DialogMessageBackToMain(JsonObject object) {
		super(object);
	}

	public DialogMessageBackToMain() {
	}

	public DialogMessageBackToMain(String message) {
		super(message);
	}

	public DialogMessageBackToMain(String message, String question) {
		super(message, question);
	}

	public DialogMessageBackToMain(String[] messages, String[] questions) {
		super(messages, questions);
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void setAsGuiActiveMessage(IDialogNpc npc, EntityPlayer player) {
		if (Minecraft.getMinecraft().currentScreen instanceof GuiDialog) {
			((GuiDialog) Minecraft.getMinecraft().currentScreen).setCurrentMessage(npc.getStartDialogMessage(player));
		}
	}
}

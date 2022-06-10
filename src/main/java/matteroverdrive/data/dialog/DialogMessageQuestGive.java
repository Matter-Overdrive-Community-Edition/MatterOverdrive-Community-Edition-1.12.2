
package matteroverdrive.data.dialog;

import com.google.gson.JsonObject;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogQuestGiver;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.gui.GuiDialog;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DialogMessageQuestGive extends DialogMessage {
	QuestStack questStack;
	boolean returnToMain;

	public DialogMessageQuestGive(JsonObject object) {
		super(object);
		this.questStack = questStack;
	}

	public DialogMessageQuestGive(String message, QuestStack questStack) {
		super(message);
		this.questStack = questStack;
	}

	public DialogMessageQuestGive(String message, String question, QuestStack questStack) {
		super(message, question);
		this.questStack = questStack;
	}

	public DialogMessageQuestGive(String[] messages, String[] questions, QuestStack questStack) {
		super(messages, questions);
		this.questStack = questStack;
	}

	@Override
	public void onInteract(IDialogNpc npc, EntityPlayer player) {
		super.onInteract(npc, player);
		if (npc != null && npc instanceof IDialogQuestGiver && player != null && !player.world.isRemote) {
			((IDialogQuestGiver) npc).giveQuest(this, questStack, player);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void setAsGuiActiveMessage(IDialogNpc npc, EntityPlayer player) {
		if (Minecraft.getMinecraft().currentScreen instanceof GuiDialog) {
			((GuiDialog) Minecraft.getMinecraft().currentScreen)
					.setCurrentMessage(returnToMain ? npc.getStartDialogMessage(player) : this);
		}
	}

	public DialogMessageQuestGive setReturnToMain(boolean returnToMain) {
		this.returnToMain = returnToMain;
		return this;
	}
}

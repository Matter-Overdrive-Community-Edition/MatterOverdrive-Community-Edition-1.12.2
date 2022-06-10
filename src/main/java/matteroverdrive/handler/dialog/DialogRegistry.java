
package matteroverdrive.handler.dialog;

import java.util.HashMap;
import java.util.Map;

import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.dialog.IDialogRegistry;
import matteroverdrive.api.exceptions.MORuntimeException;
import net.minecraft.util.ResourceLocation;

public class DialogRegistry implements IDialogRegistry {
	private final Map<Integer, IDialogMessage> messageMap;
	private final Map<IDialogMessage, Integer> messageIntegerMap;
	private final Map<ResourceLocation, IDialogMessage> namedDialogMessages;
	private int nextMessageID = 0;

	public DialogRegistry() {
		messageMap = new HashMap<>();
		messageIntegerMap = new HashMap<>();
		namedDialogMessages = new HashMap<>();
	}

	public IDialogMessage getMessage(int id) {
		return messageMap.get(id);
	}

	public IDialogMessage getMessage(ResourceLocation id) {
		return namedDialogMessages.get(id);
	}

	public int getMessageId(IDialogMessage dialogMessage) {
		Integer id = messageIntegerMap.get(dialogMessage);
		if (id == null) {
			return -1;
		} else {
			return id;
		}
	}

	public void registerMessage(IDialogMessage message) {
		messageMap.put(nextMessageID, message);
		messageIntegerMap.put(message, nextMessageID);
		nextMessageID++;
	}

	@Override
	public void registerMessage(ResourceLocation name, IDialogMessage message) {
		registerMessage(message);
		if (namedDialogMessages.containsKey(name)) {
			throw new MORuntimeException(String.format("Dialog Message name '%s' already present", name));
		}
		namedDialogMessages.put(name, message);
	}
}

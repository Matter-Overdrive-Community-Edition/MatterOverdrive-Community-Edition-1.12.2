
package matteroverdrive.data.dialog;

import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import net.minecraft.entity.player.EntityPlayer;

public class DialogMessageAndroidOnly extends DialogMessage {

    public DialogMessageAndroidOnly() {
        super();
    }

    public DialogMessageAndroidOnly(String message) {
        super(message);
    }

    public DialogMessageAndroidOnly(String message, String question) {
        super(message, question);
    }

    @Override
    public boolean isVisible(IDialogNpc npc, EntityPlayer player) {
        return MOPlayerCapabilityProvider.GetAndroidCapability(player) != null && MOPlayerCapabilityProvider.GetAndroidCapability(player).isAndroid();
    }
}

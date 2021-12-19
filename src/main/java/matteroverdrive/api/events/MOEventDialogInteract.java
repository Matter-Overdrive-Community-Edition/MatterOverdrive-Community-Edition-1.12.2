
package matteroverdrive.api.events;

import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogOption;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

public class MOEventDialogInteract extends PlayerEvent {
    public final IDialogNpc npc;
    public final IDialogOption dialogOption;
    public final Side side;

    public MOEventDialogInteract(EntityPlayer player, IDialogNpc npc, IDialogOption dialogOption, Side side) {
        super(player);
        this.npc = npc;
        this.dialogOption = dialogOption;
        this.side = side;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}

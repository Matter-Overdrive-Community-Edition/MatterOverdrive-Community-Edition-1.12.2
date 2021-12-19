
package matteroverdrive.data.dialog;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class DialogMessageAndroidTransformation extends DialogMessage {
    public DialogMessageAndroidTransformation() {
        super();
    }

    public DialogMessageAndroidTransformation(String message, String question) {
        super(message, question);
    }

    public DialogMessageAndroidTransformation(String message) {
        super(message);
    }

    @Override
    public boolean canInteract(IDialogNpc npc, EntityPlayer player) {
        boolean[] hasParts = new boolean[4];
        int[] slots = new int[4];

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() == MatterOverdrive.ITEMS.androidParts) {
                int damage = player.inventory.getStackInSlot(i).getItemDamage();
                if (damage < hasParts.length) {
                    hasParts[damage] = true;
                    slots[damage] = i;
                }
            }
        }

        for (boolean hasPart : hasParts) {
            if (!hasPart) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onInteract(IDialogNpc npc, EntityPlayer player) {
        boolean[] hasParts = new boolean[4];
        int[] slots = new int[4];

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() == MatterOverdrive.ITEMS.androidParts) {
                int damage = player.inventory.getStackInSlot(i).getItemDamage();
                if (damage < hasParts.length) {
                    hasParts[damage] = true;
                    slots[damage] = i;
                }
            }
        }

        for (boolean hasPart : hasParts) {
            if (!hasPart) {
                if (!player.world.isRemote) {
                    TextComponentString componentText = new TextComponentString(TextFormatting.GOLD + "<Mad Scientist>" + TextFormatting.RED + MOStringHelper.translateToLocal("entity.mad_scientist.line.fail." + player.getRNG().nextInt(4)));
                    componentText.setStyle(new Style().setColor(TextFormatting.RED));
                    player.sendMessage(componentText);
                }
                return;
            }
        }

        if (!player.world.isRemote) {
            for (int slot : slots) {
                player.inventory.decrStackSize(slot, 1);
            }
        }

        MOPlayerCapabilityProvider.GetAndroidCapability(player).startConversion();
        player.closeScreen();
    }

    @Override
    public boolean isVisible(IDialogNpc npc, EntityPlayer player) {
        return MOPlayerCapabilityProvider.GetAndroidCapability(player) == null || !MOPlayerCapabilityProvider.GetAndroidCapability(player).isAndroid();
    }
}

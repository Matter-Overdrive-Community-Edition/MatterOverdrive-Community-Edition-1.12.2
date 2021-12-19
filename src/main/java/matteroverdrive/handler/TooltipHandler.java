
package matteroverdrive.handler;

import matteroverdrive.api.events.MOEventMatterTooltip;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class TooltipHandler {
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            MOEventMatterTooltip tooltipEvent = new MOEventMatterTooltip(event.getItemStack(), MatterHelper.getMatterAmountFromItem(event.getItemStack()), event.getEntityPlayer());
            if (!MinecraftForge.EVENT_BUS.post(tooltipEvent)) {
                if (tooltipEvent.matter > 0) {
                    event.getToolTip().add(TextFormatting.BLUE + MOStringHelper.translateToLocal("gui.tooltip.matter") + ": " + TextFormatting.GOLD + MatterHelper.formatMatter(tooltipEvent.matter));
                } else {
                    event.getToolTip().add(TextFormatting.BLUE + MOStringHelper.translateToLocal("gui.tooltip.matter") + ": " + TextFormatting.RED + MOStringHelper.translateToLocal("gui.tooltip.matter.none"));
                }
            }
        }
    }
}

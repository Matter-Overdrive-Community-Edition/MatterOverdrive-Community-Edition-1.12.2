
package matteroverdrive.guide;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class GuideElementTooltip extends GuideElementAbstract {
    ItemStack itemStack;
    List<String> lines;

    public GuideElementTooltip() {
        lines = new ArrayList<>();

    }

    @Override
    protected void loadContent(MOGuideEntry entry, Element element, int width, int height) {
        if (element.hasAttribute("item")) {
            itemStack = shortCodeToStack(decodeShortcode(element.getAttribute("item")));
        } else {
            itemStack = entry.getStackIcons()[0];
        }

        itemStack.getItem().addInformation(itemStack, Minecraft.getMinecraft().world, lines, ITooltipFlag.TooltipFlags.ADVANCED);
        this.height = lines.size() * getFontRenderer().FONT_HEIGHT;
    }

    @Override
    public void drawElement(int width, int mouseX, int mouseY) {
        for (int i = 0; i < lines.size(); i++) {
            getFontRenderer().drawString(lines.get(i), x, y + i * getFontRenderer().FONT_HEIGHT, color.getColor());
        }
    }
}

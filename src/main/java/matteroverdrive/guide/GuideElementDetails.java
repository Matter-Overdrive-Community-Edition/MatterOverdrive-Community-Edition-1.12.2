
package matteroverdrive.guide;

import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class GuideElementDetails extends GuideElementAbstract {
	List<String> details;

	@Override
	protected void loadContent(MOGuideEntry entry, Element element, int width, int height) {
		if (element.hasAttribute("item")) {
			ItemStack stack = shortCodeToStack(decodeShortcode(element.getAttribute("item")));
			if (!stack.isEmpty() && stack.getItem() != null && stack.getItem() instanceof MOBaseItem) {
				List<String> details = new ArrayList<>();
				((MOBaseItem) stack.getItem()).addDetails(stack, Minecraft.getMinecraft().player,
						Minecraft.getMinecraft().world, details);
				this.details = details;
				for (String detail : details) {
					width = Math.max(getFontRenderer().getStringWidth(detail), width);
				}
				this.height = details.size() * getFontRenderer().FONT_HEIGHT;
			}
		}
	}

	@Override
	public void drawElement(int width, int mouseX, int mouseY) {
		for (int i = 0; i < details.size(); i++) {
			getFontRenderer().drawString(details.get(i), marginLeft, marginTop + i * getFontRenderer().FONT_HEIGHT,
					color.getColor());
		}
	}
}

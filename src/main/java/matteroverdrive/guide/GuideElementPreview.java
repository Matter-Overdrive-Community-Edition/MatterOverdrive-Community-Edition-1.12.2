
package matteroverdrive.guide;

import java.util.Random;

import org.w3c.dom.Element;

import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class GuideElementPreview extends GuideElementAbstract {
	private static Random random = new Random();
	double size = 1;
	ItemStack itemStack;

	@Override
	public void drawElement(int width, int mouseX, int mouseY) {
		if (itemStack != null) {
			GlStateManager.pushMatrix();

			if (textAlign == 1) {
				GlStateManager.translate(width / 2 - 8 * size, 0, 0);
			} else if (textAlign == 2) {
				GlStateManager.translate(width - 16 * size, 0, 0);
			}

			GlStateManager.translate(marginLeft, marginTop, 0);
			GlStateManager.scale(size, size, size);
			RenderUtils.renderStack(0, 0, 0, itemStack, false);
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected void loadContent(MOGuideEntry entry, Element element, int width, int height) {
		if (element.hasAttribute("item")) {
			itemStack = shortCodeToStack(decodeShortcode(element.getAttribute("item")));
		} else if (entry.getStackIcons() != null && entry.getStackIcons().length > 0) {
			int index = random.nextInt(entry.getStackIcons().length);
			if (element.hasAttribute("index")) {
				index = Integer.parseInt(element.getAttribute("index"));
			}
			index = MathHelper.clamp(index, 0, entry.getStackIcons().length - 1);
			itemStack = entry.getStackIcons()[index];
		}
		if (element.hasAttribute("size")) {
			size = Double.parseDouble(element.getAttribute("size"));
		}
		this.height = (int) (16 * size);
		this.width = (int) (16 * size);
	}
}

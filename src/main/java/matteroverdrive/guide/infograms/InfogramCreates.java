
package matteroverdrive.guide.infograms;

import matteroverdrive.Reference;
import matteroverdrive.guide.GuideElementAbstract;
import matteroverdrive.guide.MOGuideEntry;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.w3c.dom.Element;

public class InfogramCreates extends GuideElementAbstract {
	private static final ResourceLocation background = new ResourceLocation(
			Reference.PATH_ELEMENTS + "guide_info_creates.png");
	ItemStack from;
	ItemStack to;

	@Override
	public void drawElement(int width, int mouseX, int mouseY) {
		bindTexture(background);
		GlStateManager.color(1, 1, 1);
		RenderUtils.drawPlane(marginLeft, marginTop, 0, 115, 36);
		if (from != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(marginLeft + 5, marginTop + 5, 0);
			GlStateManager.scale(1.5, 1.5, 1.5);
			RenderUtils.renderStack(0, 0, from);
			GlStateManager.popMatrix();
		}
		if (to != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(marginLeft + 86, marginTop + 5, 0);
			GlStateManager.scale(1.5, 1.5, 1.5);
			RenderUtils.renderStack(0, 0, to);
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected void loadContent(MOGuideEntry entry, Element element, int width, int height) {
		if (element.hasAttribute("to")) {
			to = shortCodeToStack(decodeShortcode(element.getAttribute("to")));
			if (to == null) {
				MOLog.warn("Invalid to Itemstack in infogram of type create: %s", element.hasAttribute("to"));
			}
		} else {
			MOLog.warn("There is no to Itemstack in infogram of type create");
		}

		if (element.hasAttribute("from")) {
			from = shortCodeToStack(decodeShortcode(element.getAttribute("from")));
			if (from == null) {
				MOLog.warn("Invalid from Itemstack in infogram of type create: %", element.getAttribute("from"));
			}
		} else {
			if (entry.getStackIcons().length > 0) {
				from = entry.getStackIcons()[0];
			}
		}
		this.height = 36 + 16;
		this.width = 100;
	}
}

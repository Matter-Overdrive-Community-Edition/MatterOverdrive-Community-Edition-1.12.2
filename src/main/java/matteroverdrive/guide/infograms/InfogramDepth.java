
package matteroverdrive.guide.infograms;

import matteroverdrive.Reference;
import matteroverdrive.guide.GuideElementAbstract;
import matteroverdrive.guide.MOGuideEntry;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.w3c.dom.Element;

public class InfogramDepth extends GuideElementAbstract {
	private static final ResourceLocation terrain_background = new ResourceLocation(
			Reference.PATH_ELEMENTS + "guide_info_depth_terrain.png");
	private static final ResourceLocation terrain_background_stripes = new ResourceLocation(
			Reference.PATH_ELEMENTS + "guide_info_depth_terrain_stripes.png");
	private static final ResourceLocation ore_lense_background = new ResourceLocation(
			Reference.PATH_ELEMENTS + "guide_info_depth_ore_lense.png");
	private ItemStack stack;
	private int minDepth = 0;
	private int maxDepth = 64;

	@Override
	public void drawElement(int width, int mouseX, int mouseY) {
		float minPercent = 1 - Math.min(minDepth, 64) / 64f;
		float maxPercent = 1 - Math.min(maxDepth, 64) / 64f;

		if (maxDepth >= 0) {
			getFontRenderer().drawString(Integer.toString(maxDepth) + "-", 8, 8 + (int) (46 * maxPercent),
					Reference.COLOR_HOLO_GREEN.getColor());
		}
		if (minDepth > 0) {
			getFontRenderer().drawString(Integer.toString(minDepth) + "-", 8, 8 + (int) (46 * minPercent),
					Reference.COLOR_HOLO_GREEN.getColor());
		}

		bindTexture(terrain_background);
		GlStateManager.color(1, 1, 1);
		RenderUtils.drawPlane(marginLeft + 20.5, marginTop + 8, 0, 72.5, 53);

		RenderUtils.beginStencil();
		RenderUtils.drawStencil(marginLeft + 24, marginTop + 8 + (int) (53 * maxPercent), 24 + 69,
				8 + (int) (53 * minPercent), 1);
		bindTexture(terrain_background_stripes);
		GlStateManager.color(1, 1, 1);
		RenderUtils.drawPlane(marginLeft + 24, marginTop + 8, 0, 69, 53);
		RenderUtils.endStencil();
		bindTexture(ore_lense_background);
		GlStateManager.color(1, 1, 1);
		RenderUtils.drawPlane(marginLeft + 69, marginTop + 16, 0, 84, 41);

		if (!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(marginLeft + 123, marginTop + 21, 0);
			GlStateManager.scale(1.5, 1.5, 1.5);
			RenderUtils.renderStack(0, 0, stack);
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected void loadContent(MOGuideEntry entry, Element element, int width, int height) {
		if (element.hasAttribute("min")) {
			minDepth = Integer.parseInt(element.getAttribute("min"));
		}
		if (element.hasAttribute("max")) {
			maxDepth = Integer.parseInt(element.getAttribute("max"));
		}
		if (element.hasAttribute("item")) {
			stack = shortCodeToStack(decodeShortcode(element.getAttribute("item")));
		} else {
			if (entry.getStackIcons().length > 0) {
				stack = entry.getStackIcons()[0];
			}
		}
		this.width = 100;
		this.height = 53 + 16;
	}
}

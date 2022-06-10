
package matteroverdrive.gui.element;

import matteroverdrive.Reference;
import matteroverdrive.gui.MOGuiBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ElementIndicator extends MOElementBase {
	public static final ResourceLocation BG = new ResourceLocation(Reference.PATH_ELEMENTS + "indicator.png");
	private int indication;

	public ElementIndicator(MOGuiBase gui, int posX, int posY) {
		super(gui, posX, posY, 21, 5);
		this.texH = 15;
		this.texW = 21;
	}

	@Override
	public void updateInfo() {

	}

	@Override
	public void init() {

	}

	@Override
	public void addTooltip(List<String> var1, int mouseX, int mouseY) {

	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {
		GlStateManager.color(1, 1, 1);
		gui.bindTexture(BG);
		gui.drawSizedTexturedModalRect(posX, posY, 0, 5 * indication, sizeX, sizeY, texW, texH);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {

	}

	public void setIndication(int indication) {
		this.indication = indication;
	}
}

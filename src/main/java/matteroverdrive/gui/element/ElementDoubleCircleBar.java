
package matteroverdrive.gui.element;

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.gui.MOGuiBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_GREATER;

public class ElementDoubleCircleBar extends MOElementBase {
	public static final ResourceLocation BG = new ResourceLocation(Reference.PATH_ELEMENTS + "circle_bar.png");
	public static final ResourceLocation OVERLAY = new ResourceLocation(Reference.PATH_ELEMENTS + "circle_bar_top.png");
	public static final ResourceLocation BACK = new ResourceLocation(Reference.PATH_ELEMENTS + "circle_bar_bottom.png");
	private float progressLeft;
	private float progressRight;

	private Color colorLeft;
	private Color colorRight;

	public ElementDoubleCircleBar(MOGuiBase gui, int posX, int posY, int width, int height, Color color) {
		super(gui, posX, posY, width, height);
		colorLeft = color;
		colorRight = color;
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
		gui.bindTexture(BACK);
		GlStateManager.color(1, 1, 1, 1);
		gui.drawSizedTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY, 135, 135);

		// left
		gui.bindTexture(BG);
		GlStateManager.alphaFunc(GL_GREATER, 1 - progressLeft);
		if (colorLeft != null) {
			GlStateManager.color(colorLeft.getFloatR(), colorLeft.getFloatG(), colorLeft.getFloatB(),
					colorLeft.getFloatA());
		}
		gui.drawSizedTexturedModalRect(posX, posY, 0, 0, sizeX / 2, sizeY, 135, 135);
		GlStateManager.alphaFunc(GL_GREATER, 0.2f);

		// right
		gui.bindTexture(BG);
		GlStateManager.alphaFunc(GL_GREATER, 1 - progressRight);
		if (colorRight != null) {
			GlStateManager.color(colorRight.getFloatR(), colorRight.getFloatG(), colorRight.getFloatB(),
					colorRight.getFloatA());
		}
		gui.drawSizedTexturedModalRect(posX + 135 / 2, posY, 135 / 2, 0, sizeX / 2, sizeY, 135, 135);
		GlStateManager.alphaFunc(GL_GREATER, 0.2f);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		gui.bindTexture(OVERLAY);
		GlStateManager.color(1, 1, 1, 1);
		gui.drawSizedTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY, 135, 135);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}

	public void setColorLeft(Color color) {
		this.colorLeft = color;
	}

	public void setColorRight(Color color) {
		this.colorRight = color;
	}

	public float getProgressLeft() {
		return progressLeft;
	}

	public void setProgressLeft(float progressLeft) {
		this.progressLeft = progressLeft;
	}

	public float getProgressRight() {
		return progressRight;
	}

	public void setProgressRight(float progressRight) {
		this.progressRight = progressRight;
	}
}

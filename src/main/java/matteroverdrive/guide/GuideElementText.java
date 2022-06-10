
package matteroverdrive.guide;

import matteroverdrive.gui.GuiDataPad;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;
import org.w3c.dom.Element;

import java.util.List;

public class GuideElementText extends GuideElementTextAbstract {
	TextLine[] lines;

	protected String formatVariableReplace(String variable, String replace) {
		return TextFormatting.AQUA + replace + TextFormatting.RESET;
	}

	@Override
	public void drawElement(int width, int mouseX, int mouseY) {
		for (int i = 0; i < lines.length; i++) {
			int x = 0;
			if (textAlign == 1) {
				x = -lines[i].getWidth() / 2 + (width - marginLeft - marginRight) / 2;
			} else if (textAlign == 2) {
				x = -lines[i].getWidth() + (width - marginLeft - marginRight);
			}

			for (int c = 0; c < lines[i].chunks.size(); c++) {
				int y = marginTop + i * Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
				TextChunk chunk = lines[i].chunks.get(c);

				if (chunk instanceof TextChunkLink) {
					if (mouseX > x && mouseX < x + chunk.getWidth() && mouseY > y
							&& mouseY < y + getFontRenderer().FONT_HEIGHT) {
						if (Mouse.isButtonDown(0)) {
							if (gui instanceof GuiDataPad) {
								((TextChunkLink) chunk).onClick((GuiDataPad) gui);
							}
						}

					}
				}
				Minecraft.getMinecraft().fontRenderer.drawString(lines[i].chunks.get(c).getText(), marginLeft + x, y,
						color.getColor());
				int w = calculateWidth(null, lines[i].chunks.get(c), null);
				if (c > 0 && c < lines[i].chunks.size() - 1) {
					w = calculateWidth(lines[i].chunks.get(c - 1), lines[i].chunks.get(c), lines[i].chunks.get(c + 1));
				}
				x += w;
			}
		}
	}

	@Override
	protected void loadContent(MOGuideEntry entry, Element element, int width, int height) {
		List<TextLine> lines = handleTextFormatting(entry, element.getTextContent(), this.width);
		this.lines = new TextLine[lines.size()];
		this.lines = lines.toArray(this.lines);
		this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * this.lines.length;
	}
}


package matteroverdrive.guide;

import java.util.Map;

import org.w3c.dom.Element;

import matteroverdrive.gui.MOGuiBase;

public interface IGuideElement {
	void setGUI(MOGuiBase gui);

	void loadElement(MOGuideEntry entry, Element element, Map<String, String> styleSheetMap, int width, int height);

	void drawElement(int width, int mouseX, int mouseY);

	int getHeight();

	int getWidth();

	int getFloating();
}


package matteroverdrive.gui.element;

import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.guide.GuideCategory;

public class ElementGuideCategory extends MOElementButtonScaled {
	private final GuideCategory category;

	public ElementGuideCategory(MOGuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sizeX,
			int sizeY, GuideCategory category) {
		super(gui, handler, posX, posY, name, sizeX, sizeY);
		this.category = category;
		this.icon = category.getHoloIcon();
		this.setToolTip(category.getDisplayName());
	}

	public GuideCategory getCategory() {
		return category;
	}
}

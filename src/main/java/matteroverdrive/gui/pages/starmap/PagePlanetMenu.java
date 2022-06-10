
package matteroverdrive.gui.pages.starmap;

import matteroverdrive.container.ContainerStarMap;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.StarmapHelper;
import net.minecraft.client.renderer.GlStateManager;

public class PagePlanetMenu extends ElementBaseGroup {

	private TileEntityMachineStarMap starMap;

	public PagePlanetMenu(MOGuiBase gui, int posX, int posY, int width, int height, ContainerStarMap starMapContainer,
			TileEntityMachineStarMap starMap) {
		super(gui, posX, posY, width, height);
		this.starMap = starMap;
	}

	@Override
	public void update(int mouseX, int mouseY, float partialTicks) {
		super.update(mouseX, mouseY, partialTicks);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		super.drawForeground(mouseX, mouseY);
		if (starMap.getPlanet() != null) {
			GlStateManager.pushMatrix();
			int width = getFontRenderer().getStringWidth(starMap.getPlanet().getSpaceBodyName());
			GlStateManager.translate(sizeY / 2 + width / 2, 16, 0);
			GlStateManager.scale(1, 1, 1);
			StarmapHelper.drawPlanetInfo(starMap.getPlanet(), starMap.getPlanet().getSpaceBodyName(), 12 - width / 2,
					4);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void init() {
		super.init();
	}
}

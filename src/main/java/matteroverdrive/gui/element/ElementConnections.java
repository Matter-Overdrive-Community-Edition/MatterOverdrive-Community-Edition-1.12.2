
package matteroverdrive.gui.element;

import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.tile.TileEntityMachinePacketQueue;

import java.util.List;

public class ElementConnections extends MOElementBase {
	TileEntityMachinePacketQueue machine;

	public ElementConnections(MOGuiBase gui, int posX, int posY, int width, int height,
			TileEntityMachinePacketQueue machine) {
		super(gui, posX, posY, width, height);
		this.machine = machine;
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
		/*
		 * for (int i = 0;i < 6;i++) { BlockPos connection = machine.getConnection(i);
		 * GL11.glColor3f(1, 1, 1); int x = 50; int y = 42 + i * 19;
		 * 
		 * if (connection != null) { String info = EnumFacing.getOrientation(i).name() +
		 * " : " +
		 * connection.getTileEntity(machine.getworld()).getBlockType().getLocalizedName(
		 * );
		 * 
		 * MOElementButton.NORMAL_TEXTURE.render(x - 6,y - 6,sizeX + 12,19);
		 * getFontRenderer().drawString(info,x,y,0xFFFFFF); } else {
		 * MOElementButton.HOVER_TEXTURE_DARK.render(x - 6,y - 6,80,19);
		 * getFontRenderer().drawString(EnumFacing.getOrientation(i).name() + " : None",
		 * x, y, 0xFFFFFF); } }
		 */
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {

	}
}

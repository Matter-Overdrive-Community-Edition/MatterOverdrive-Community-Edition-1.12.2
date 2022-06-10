
package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.tile.TileEntityMachineContractMarket;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.glColor3f;

public class TileEntityRendererContractMarket extends TileEntityRendererMonitor<TileEntityMachineContractMarket> {
	public static final ResourceLocation screenTexture = new ResourceLocation(
			Reference.PATH_BLOCKS + "contract_station_holo.png");

	@Override
	public void drawScreen(TileEntityMachineContractMarket tile, float ticks) {
		Minecraft.getMinecraft().renderEngine.bindTexture(screenTexture);
		glColor3f(Reference.COLOR_HOLO.getFloatR() * 0.7f, Reference.COLOR_HOLO.getFloatG() * 0.7f,
				Reference.COLOR_HOLO.getFloatB() * 0.7f);

		RenderUtils.drawPlane(1);
	}
}
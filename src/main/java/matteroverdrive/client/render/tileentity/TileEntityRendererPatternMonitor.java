
package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.machines.pattern_monitor.TileEntityMachinePatternMonitor;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.glColor3f;

import java.text.DecimalFormat;

public class TileEntityRendererPatternMonitor extends TileEntityRendererMonitor<TileEntityMachinePatternMonitor> {
	public static final ResourceLocation screenTexture = new ResourceLocation(
			Reference.PATH_BLOCKS + "pattern_monitor_holo.png");

	@Override
	public void drawScreen(TileEntityMachinePatternMonitor tileEntity, float ticks) {
		int count = tileEntity.getCount();
		Minecraft.getMinecraft().renderEngine.bindTexture(screenTexture);
		glColor3f(Reference.COLOR_HOLO.getFloatR() * 0.7f, Reference.COLOR_HOLO.getFloatG() * 0.7f,
				Reference.COLOR_HOLO.getFloatB() * 0.7f);
		DecimalFormat dF = new DecimalFormat("#.###");
		RenderUtils.drawPlane(1);
		GlStateManager.pushMatrix();
		int countWitdth = Minecraft.getMinecraft().fontRenderer.getStringWidth(dF.format(count));
		double scale = ((double) Minecraft.getMinecraft().fontRenderer
				.getStringWidth(MOStringHelper.formatNumber(10, "0")) / (double) countWitdth);
		scale = Math.min(scale, 1);
		GlStateManager.translate(0.47,
				0.33 + (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * 0.03) * (1 - scale) * 0.5, 0);
		GlStateManager.scale(scale * 0.03, scale * 0.03, scale * 0.03);
		Minecraft.getMinecraft().fontRenderer.drawString(dF.format(count), 0, 0, 0x78a1b3);
		GlStateManager.popMatrix();
	}
}
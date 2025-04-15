
package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.tile.TileEntityGravitationalAnomaly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.glu.Sphere;

public class TileEntityRendererGravitationalAnomaly extends TileEntitySpecialRenderer<TileEntityGravitationalAnomaly> {
	public static final ResourceLocation core = new ResourceLocation(
			Reference.PATH_BLOCKS + "gravitational_anomaly_core.png");
	public static final ResourceLocation anti = new ResourceLocation(
			Reference.PATH_BLOCKS + "anti_gravitational_anomaly_core.png");
	public static final ResourceLocation glow = new ResourceLocation(
			Reference.PATH_BLOCKS + "gravitational_anomaly_glow.png");
	public static final ResourceLocation black = new ResourceLocation(Reference.PATH_BLOCKS + "black.png");

	private final Sphere sphere_model;

	public TileEntityRendererGravitationalAnomaly() {
		sphere_model = new Sphere();
	}

	@Override
	public void render(TileEntityGravitationalAnomaly tileEntity, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {

		float radius = (float) tileEntity.getEventHorizon();
		renderSphere(tileEntity, x, y, z, radius);
		renderDisk(tileEntity, x, y, z, partialTicks, radius);
	}

	public void renderSphere(TileEntityGravitationalAnomaly tileEntity, double x, double y, double z,
			float visualSize) {
		if (!tileEntity.shouldRender())
			return;
		long time = Minecraft.getMinecraft().world.getWorldTime();
		float speed = 1;
		double resonateSpeed = 0.1;
		double radius = tileEntity.getEventHorizon();
		radius = radius * Math.sin(time * resonateSpeed) * 0.1 + radius * 0.9;

		// Inner
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		GlStateManager.rotate(time * speed, 0, 0, 1);
		bindTexture(black);
		GlStateManager.scale(radius, radius, radius);
		GlStateManager.color(0.0F, 0.0F, 0.0F, 1);
		sphere_model.draw((float) 0.33, 8, 8);
		GlStateManager.popMatrix();

		// Outer
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableLighting();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		GlStateManager.rotate(time * speed, 0, 1, 0);
		bindTexture(black);
		GlStateManager.scale(radius + 0.5, radius + 0.5, radius + 0.5);
		GlStateManager.color(0.0F, 0.0F, 0.2F, 0.8f);
		sphere_model.draw((float) 0.33, 8, 8);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();

	}

	public void renderDisk(TileEntityGravitationalAnomaly tileEntity, double x, double y, double z, float partialTicks,
			float visualSize) {
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
		long time = Minecraft.getMinecraft().world.getWorldTime();
		float speed = 1;
		double resonateSpeed = 0.1;
		double radius = tileEntity.getEventHorizon();
		radius = radius * Math.sin(time * resonateSpeed) * 0.1 + radius * 0.9;

		// Setup
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableLighting();

		// Translate
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		GlStateManager.rotate(time * speed, 0, 1, 0);
		// Assign texture
		this.bindTexture(core);
		GlStateManager.color(1, 0, 0, 1);

		// top render
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(-radius, 0, -radius).tex(0, 0).endVertex();
		bufferbuilder.pos(-radius, 0, +radius).tex(0, 1).endVertex();
		bufferbuilder.pos(+radius, 0, +radius).tex(1, 1).endVertex();
		bufferbuilder.pos(+radius, 0, -radius).tex(1, 0).endVertex();
		Tessellator.getInstance().draw();

		// bottom render
		GlStateManager.rotate(180, 1, 0, 0);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(-radius, 0, -radius).tex(1, 1).endVertex();
		bufferbuilder.pos(-radius, 0, +radius).tex(1, 0).endVertex();
		bufferbuilder.pos(+radius, 0, +radius).tex(0, 0).endVertex();
		bufferbuilder.pos(+radius, 0, -radius).tex(0, 1).endVertex();
		Tessellator.getInstance().draw();

		// Reset
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

}

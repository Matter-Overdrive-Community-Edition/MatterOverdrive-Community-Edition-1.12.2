
package matteroverdrive.client.render.tileentity.starmap;

import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import static org.lwjgl.opengl.GL11.*;

public class StarMapRenderPlanetStats extends StarMapRendererPlanet {
	@Override
	public void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks,
			float viewerDistance) {
		if (spaceBody instanceof Planet) {

			glLineWidth(1);

			Planet to = (Planet) spaceBody;
			Planet from = galaxy.getPlanet(starMap.getGalaxyPosition());

			if (from != null && from != to) {
				GlStateManager.pushMatrix();
				Matrix4f rotationMat = new Matrix4f();
				rotationMat.rotate(
						Minecraft.getMinecraft().getRenderViewEntity().rotationYaw * (float) (Math.PI / 180D),
						new Vector3f(0, -1, 0));
				GlStateManager.enableBlend();
				GlStateManager.pushMatrix();
				Vector4f pos = new Vector4f((float) (getClampedSize(from) + 0.25), 0, 0, 1);
				Matrix4f.transform(rotationMat, pos, pos);
				GlStateManager.translate(pos.x, pos.y, pos.z);
				renderPlanet(from, viewerDistance);
				GlStateManager.popMatrix();

				GlStateManager.pushMatrix();
				glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
				GlStateManager.enableTexture2D();
				GlStateManager.enableAlpha();
				RenderUtils.rotateTo(Minecraft.getMinecraft().getRenderViewEntity());
				GlStateManager.scale(0.01, 0.01, 0.01);
				GlStateManager.rotate(180, 0, 0, 1);
				GlStateManager.translate(-9, -9, 0);
				ClientProxy.holoIcons.renderIcon("arrow_right", 0, 0);
				GlStateManager.popMatrix();

				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL_ONE, GL_ONE);
				GlStateManager.pushMatrix();
				pos = new Vector4f((float) -(getClampedSize(to) + 0.25), 0, 0, 1);
				Matrix4f.transform(rotationMat, pos, pos);
				GlStateManager.translate(pos.x, pos.y, pos.z);
				renderPlanet(to, viewerDistance);
				GlStateManager.popMatrix();
				GlStateManager.popMatrix();
			} else {
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL_ONE, GL_ONE);
				GlStateManager.pushMatrix();
				renderPlanet(to, viewerDistance);
				GlStateManager.popMatrix();
			}

			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			GlStateManager.enableTexture2D();
		}
	}

	@Override
	protected void drawPlanetInfoClose(Planet planet) {

	}

	@Override
	public void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks,
			float opacity) {
		if (spaceBody instanceof Planet) {
			Planet planet = (Planet) spaceBody;
			int y = 0;
		}
	}

	@Override
	public boolean displayOnZoom(int zoom, SpaceBody spaceBody) {
		return zoom == 4;
	}
}

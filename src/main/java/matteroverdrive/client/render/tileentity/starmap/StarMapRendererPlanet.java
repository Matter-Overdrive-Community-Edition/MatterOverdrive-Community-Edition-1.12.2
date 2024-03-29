
package matteroverdrive.client.render.tileentity.starmap;

import matteroverdrive.Reference;
import matteroverdrive.api.starmap.IBuildable;
import matteroverdrive.api.starmap.IBuilding;
import matteroverdrive.client.data.Color;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class StarMapRendererPlanet extends StarMapRendererAbstract {

	@Override
	public void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks,
			float viewerDistance) {
		if (spaceBody instanceof Planet) {
			glLineWidth(1);

			Planet planet = (Planet) spaceBody;
			GlStateManager.pushMatrix();
			renderPlanet(planet, viewerDistance);
			GlStateManager.popMatrix();

			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			GlStateManager.enableTexture2D();

		}
	}

	protected void renderPlanet(Planet planet, float viewerDistance) {
		GlStateManager.pushMatrix();
		float size = getClampedSize(planet);
		GlStateManager.rotate(10, 1, 0, 0);

		GlStateManager.rotate(Minecraft.getMinecraft().world.getWorldTime() * 0.1f, 0, 1, 0);
		glPolygonMode(GL_FRONT, GL_LINE);

		GlStateManager.enableCull();
		// region Sphere rotated
		GlStateManager.pushMatrix();
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(true);
		GlStateManager.color(0, 0, 0);
		sphere.draw(size * 0.99f, 64, 32);
		GlStateManager.depthMask(false);

		// region Planet
		GlStateManager.pushMatrix();
		GlStateManager.rotate(90, 1, 0, 0);

		RenderUtils.applyColorWithMultipy(Planet.getGuiColor(planet), 0.2f * (1f / viewerDistance));
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		sphere.draw(size, 64, 32);
		GlStateManager.popMatrix();
		// endregion
		drawBuildings(planet, size, viewerDistance);
		GlStateManager.popMatrix();
		// endregion

		GlStateManager.disableCull();
		GlStateManager.popMatrix();

		drawPlanetInfoClose(planet);

		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		GlStateManager.enableTexture2D();
		drawShips(planet, size, viewerDistance);
	}

	protected float getClampedSize(Planet planet) {
		return Math.min(Math.max(planet.getSize(), 1f), 2.2f) * 0.5f;
	}

	 private void drawBuildings(Planet planet, float planetSize, float viewerDistance)
	    {
	        random.setSeed(planet.getSeed());
	        for (int i = 0;i < planet.getBuildings().size();i++)
	        {
	        	GlStateManager.pushMatrix();
	            glRotated(random.nextDouble() * 360, 0, 1, 0);
	            glRotated(random.nextDouble() * 360, 0, 0, 1);
	            glTranslated(planetSize - 0.04, 0, 0);
	            RenderUtils.drawCube(0.1, 0.1, 0.1, Reference.COLOR_HOLO, (1f / viewerDistance));
	            GlStateManager.popMatrix();
	        }
	    }

	    private void drawShips(Planet planet, float planetSize, float viewerDistance)
	    {
	        RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, (1f / viewerDistance));
	        random.setSeed(planet.getSeed());
	        for (int i = 0; i < planet.getFleet().size();i++)
	        {
	        	GlStateManager.pushMatrix();
	            double direction = random.nextDouble() * 2 - 1;
	            double startingAngle = random.nextDouble() * Math.PI*2;
	            double phi = startingAngle + Math.copySign(Minecraft.getMinecraft().world.getWorldTime() * 0.005, direction);
	            double theta = random.nextDouble() * Math.PI * 2;
	            double radius = random.nextDouble() * 0.3 + 0.1  + planetSize;
	            Vector3f pos = new Vector3f((float)(Math.sin(phi) * Math.sin(theta) * radius), (float)(Math.sin(phi) * Math.cos(theta) * radius), (float)(Math.cos(phi) * radius));
	            renderShipPath(planet, planet.getShip(i) ,phi, theta, direction, radius);
	            glTranslatef(pos.x, pos.y, pos.z);
	            GlStateManager.pushMatrix();
	            glScaled(0.01, 0.01, 0.01);
	            glRotated(Minecraft.getMinecraft().getRenderViewEntity().rotationYaw, 0, -1, 0);
	            glRotated(Minecraft.getMinecraft().getRenderViewEntity().rotationPitch, 1, 0, 0);
	            glRotated(180, 0, 0, 1);
	            glTranslated(-8, -8, 0);
	            RenderUtils.renderStack(0, 0, 0 , planet.getShip(i),false);
	            GlStateManager.popMatrix();
	            GlStateManager.popMatrix();
	        }
	    }

	    protected void renderShipPath(Planet planet, ItemStack shipStack, double phi, double theta, double direction, double radius)
	    {
	    	GlStateManager.disableTexture2D();
	        GlStateManager.enableBlend();
	        GlStateManager.blendFunc(GL_ONE, GL_ONE);
	        RenderUtils.applyColorWithMultipy(Planet.getGuiColor(planet), 0.2f);
	        glBegin(GL_LINE_STRIP);
	        for (int p = 0;p < 8;p++)
	        {
	            double newPhi = phi - Math.copySign(0.1 * p ,direction);
	            Vector3f pathPos = new Vector3f((float)(Math.sin(newPhi) * Math.sin(theta) * radius), (float)(Math.sin(newPhi) * Math.cos(theta) * radius), (float)(Math.cos(newPhi) * radius));
	            glVertex3f(pathPos.x, pathPos.y, pathPos.z);
	        }
	        glEnd();
	        GlStateManager.enableTexture2D();

	    }

	protected void drawPlanetInfoClose(Planet planet) {
		GlStateManager.pushMatrix();
        RenderUtils.rotateTo(Minecraft.getMinecraft().getRenderViewEntity());
        GlStateManager.enableTexture2D(); //glEnable(GL_TEXTURE_2D);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        if (GalaxyClient.getInstance().canSeePlanetInfo(planet,Minecraft.getMinecraft().player))
        {
            double radius = getClampedSize(planet) * 140;
            GlStateManager.pushMatrix();
            glScaled(0.01, 0.01, 0.01);
            glRotated(180, 0, 0, 1);
            for (int i = 0; i < planet.getBuildings().size(); i++)
            {
                double angle =  14 * i - 6 * planet.getBuildings().size();
                angle *= (Math.PI / 180);
                int x = (int) (Math.cos(angle) * radius) - 10;
                int y = (int) (Math.sin(angle) * radius) - 10;
                RenderUtils.renderStack(x, y, planet.getBuildings().get(i),1);
                Color color = Reference.COLOR_HOLO_RED;
                if (planet.getBuildings().get(i).getItem() instanceof IBuilding && ((IBuilding) planet.getBuildings().get(i).getItem()).isOwner(planet.getBuildings().get(i),Minecraft.getMinecraft().player))
                    color = Reference.COLOR_HOLO;
                Minecraft.getMinecraft().fontRenderer.drawString(planet.getBuildings().get(i).getDisplayName(), x + 21, y + 6, color.getColor());

            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
		//GlStateManager.pushMatrix();
		//GlStateManager.popMatrix();
	}

	@Override
	public void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks,
			float opacity) {
		if (spaceBody instanceof Planet) {
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL_ONE, GL_ONE);

			Color color = Reference.COLOR_HOLO;
			Planet planet = (Planet) spaceBody;
			int x = 0;
			int y = -16;

            if (GalaxyClient.getInstance().canSeePlanetInfo(planet,Minecraft.getMinecraft().player)) {
                int itemCount = 0;
                for (int i = 0; i < planet.getSizeInventory(); i++) {
                    if (planet.getStackInSlot(i) != null) {
                        ItemStack stack = planet.getStackInSlot(i);
                        List<String> info = new ArrayList<>();
                        if (stack.getItem() instanceof IBuildable && planet.canBuild((IBuildable)stack.getItem(),stack,info)) {
                            RenderUtils.renderStack(0, y - itemCount * 18 - 21,0, stack,false);
                            glEnable(GL_BLEND);
                            glBlendFunc(GL_ONE, GL_ONE);
                            RenderUtils.drawString(String.format("%1$s - %2$s", stack.getDisplayName(), MOStringHelper.formatRemainingTime(((IBuildable) stack.getItem()).getRemainingBuildTimeTicks(stack, planet, Minecraft.getMinecraft().world)/20)), 0 + 18, y + 5 - itemCount * 18 - 21, Reference.COLOR_HOLO, opacity);
                        }else
                        {
                            RenderUtils.renderStack(0, y - itemCount * 18 - 21,0, stack,false);
                            glEnable(GL_BLEND);
                            glBlendFunc(GL_ONE, GL_ONE);
                            RenderUtils.drawString(String.join(". ",info), 18, y + 5 - itemCount * 18 - 21, Reference.COLOR_HOLO_RED, opacity);
                        }
                        itemCount++;
                    }
                }

                int factoryCount = planet.getFactoryCount();
                if (factoryCount <= 0)
                    color = Reference.COLOR_HOLO_RED;

                RenderUtils.applyColorWithMultipy(color, opacity);
                ClientProxy.holoIcons.renderIcon("holo_factory", x, y);
                String factoryInfo = String.format("%1$s/%2$s", factoryCount, planet.getBuildingSpaces());
                x += 18;
                RenderUtils.drawString(factoryInfo, x, y+6, color, opacity);

                int fleetCount = planet.getFleetCount();
                color = Reference.COLOR_HOLO;
                if (fleetCount <= 0)
                    color = Reference.COLOR_HOLO_RED;

                String fleetInfo = String.format("%1$s/%2$s", fleetCount, planet.getFleetSpaces());
                RenderUtils.applyColorWithMultipy(color, opacity);
                x += fontRenderer.getStringWidth(factoryInfo) + 8;
                ClientProxy.holoIcons.renderIcon("icon_shuttle", x, y);
                x += 18;
                RenderUtils.drawString(fleetInfo, x, y+6, color, opacity);

                x += fontRenderer.getStringWidth(fleetInfo) + 8;
            }

			RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, opacity);
			ClientProxy.holoIcons.renderIcon("icon_size", x, y);
			x += 18;
			RenderUtils.drawString(DecimalFormat.getPercentInstance().format(planet.getSize()), x, y + 6,
					Reference.COLOR_HOLO, opacity);

			if (GalaxyClient.getInstance().canSeePlanetInfo(planet, Minecraft.getMinecraft().player)) {
				x = -2;
				y -= 20;

				float happines = planet.getHappiness();
				color = RenderUtils.lerp(Reference.COLOR_HOLO_RED, Reference.COLOR_HOLO,
						MathHelper.clamp(happines, 0, 1));
				RenderUtils.applyColorWithMultipy(color, opacity);
				ClientProxy.holoIcons.renderIcon("smile", x, y);
				x += 18;
				String info = DecimalFormat.getPercentInstance().format(happines);
				RenderUtils.drawString(info, x, y + 6, color, opacity);
				x += fontRenderer.getStringWidth(DecimalFormat.getPercentInstance().format(happines)) + 4;
				int population = planet.getPopulation();
				RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, opacity);
				ClientProxy.holoIcons.renderIcon("sort_random", x, y);
				x += 18;
				info = String.format("%,d", population);
				RenderUtils.drawString(info, x, y + 6, Reference.COLOR_HOLO, opacity);

				x = -3;
				y -= 20;

				int powerProduction = planet.getPowerProduction();
				RenderUtils.applyColorWithMultipy(powerProduction < 0 ? Reference.COLOR_HOLO_RED : Reference.COLOR_HOLO,
						opacity);
				ClientProxy.holoIcons.renderIcon("battery", x, y);
				x += 18;
				info = Integer.toString(powerProduction) + "m" + MOEnergyHelper.ENERGY_UNIT;
				RenderUtils.drawString(info, x, y + 6,
						powerProduction < 0 ? Reference.COLOR_HOLO_RED : Reference.COLOR_HOLO, opacity);
			}
		}
	}

	@Override
	public boolean displayOnZoom(int zoom, SpaceBody spaceBody) {
		return zoom == 3;
	}

	@Override
	public double getHologramHeight(SpaceBody spaceBody) {
		return 1.5;
	}
}


package matteroverdrive.util;

import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.client.Minecraft;

public class StarmapHelper {
	public static void drawPlanetInfo(Planet planet, String text, int x, int y) {
		drawPlanetInfo(planet, text, x, y, 1);
	}

	public static void drawPlanetInfo(Planet planet, String text, int x, int y, float multiply) {
		drawPlanetInfo(planet, text, x, y, 1, false);
	}

	public static void drawPlanetInfo(Planet planet, String text, int x, int y, float multiply, boolean unicode) {
		boolean lastUnicode;
		if (GalaxyClient.getInstance().canSeePlanetInfo(planet, Minecraft.getMinecraft().player)
				|| Minecraft.getMinecraft().player.capabilities.isCreativeMode) {
			lastUnicode = Minecraft.getMinecraft().fontRenderer.getUnicodeFlag();
			Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(unicode);
			Minecraft.getMinecraft().fontRenderer.drawString(text, x, y,
					Planet.getGuiColor(planet).multiplyWithoutAlpha(multiply).getColor());
			Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(lastUnicode);
		} else {
			lastUnicode = Minecraft.getMinecraft().standardGalacticFontRenderer.getUnicodeFlag();
			Minecraft.getMinecraft().standardGalacticFontRenderer.setUnicodeFlag(unicode);
			Minecraft.getMinecraft().standardGalacticFontRenderer.drawString(text, x, y,
					Planet.getGuiColor(planet).multiplyWithoutAlpha(multiply).getColor());
			Minecraft.getMinecraft().standardGalacticFontRenderer.setUnicodeFlag(lastUnicode);
		}
	}
}


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
		Minecraft mc = Minecraft.getMinecraft();
		if (GalaxyClient.getInstance().canSeePlanetInfo(planet, mc.player)
				|| mc.player.capabilities.isCreativeMode) {
			lastUnicode = mc.fontRenderer.getUnicodeFlag();
			mc.fontRenderer.setUnicodeFlag(unicode);
			mc.fontRenderer.drawString(text, x, y,
					Planet.getGuiColor(planet).multiplyWithoutAlpha(multiply).getColor());
			mc.fontRenderer.setUnicodeFlag(lastUnicode);
		} else {
			lastUnicode = mc.standardGalacticFontRenderer.getUnicodeFlag();
			mc.standardGalacticFontRenderer.setUnicodeFlag(unicode);
			mc.standardGalacticFontRenderer.drawString(text, x, y,
					Planet.getGuiColor(planet).multiplyWithoutAlpha(multiply).getColor());
			mc.standardGalacticFontRenderer.setUnicodeFlag(lastUnicode);
		}
	}
}

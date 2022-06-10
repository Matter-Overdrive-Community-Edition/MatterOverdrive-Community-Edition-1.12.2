
package matteroverdrive.gui;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class GuiConfig extends net.minecraftforge.fml.client.config.GuiConfig {

	public GuiConfig(GuiScreen parent) {
		super(parent, getAllGuiCategories(), Reference.MOD_ID, false, false, "Matter Overdrive Configurations");
	}

	public GuiConfig(GuiScreen parent, String category) {
		super(parent, getConfigElements(parent, category), Reference.MOD_ID, false, false,
				GuiConfig.getAbridgedConfigPath(MatterOverdrive.CONFIG_HANDLER.toString()),
				Reference.MOD_NAME + " Configurations");
	}

	private static List<IConfigElement> getConfigElements(GuiScreen parent, String category) {

		List<IConfigElement> list = new ArrayList<>();
		list.add(new ConfigElement(MatterOverdrive.CONFIG_HANDLER.getCategory(category)));
		return list;
	}

	private static List<IConfigElement> getAllGuiCategories() {
		List<IConfigElement> list = new ArrayList<>();
		MatterOverdrive.CONFIG_HANDLER.addCategoryToGui(list);
		return list;
	}
}

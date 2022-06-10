
package matteroverdrive.init;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.data.recipes.InscriberRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MatterOverdriveRecipes {
	public static final InscriberRecipeManager INSCRIBER = new InscriberRecipeManager();

	public static void registerMachineRecipes(FMLInitializationEvent event) {

		// Furnace
		GameRegistry.addSmelting(new ItemStack(MatterOverdrive.ITEMS.tritanium_dust),
				new ItemStack(MatterOverdrive.ITEMS.tritanium_ingot), 5);
		GameRegistry.addSmelting(new ItemStack(MatterOverdrive.BLOCKS.tritaniumOre),
				new ItemStack(MatterOverdrive.ITEMS.tritanium_ingot), 10);

		// Inscriber
		File file = new File(MatterOverdrive.CONFIG_HANDLER.configDir, "MatterOverdrive/recipes/inscriber.xml");
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
				IOUtils.copy(MatterOverdriveRecipes.class.getResourceAsStream(
						"/assets/matteroverdrive/recipes/inscriber.xml"), new FileOutputStream(file));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		INSCRIBER.load(file);
	}
}
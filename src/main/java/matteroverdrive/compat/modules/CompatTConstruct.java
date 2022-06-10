
package matteroverdrive.compat.modules;

import matteroverdrive.compat.Compat;
import matteroverdrive.init.OverdriveFluids;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Compat(CompatTConstruct.ID)
public class CompatTConstruct {

	public static final String ID = "tconstruct";

	@Compat.PreInit
	public static void preInit(FMLPreInitializationEvent event) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("fluid", OverdriveFluids.moltenTritanium.getName());
		tag.setString("ore", "Tritanium");
		tag.setBoolean("toolforge", true);

		FMLInterModComms.sendMessage(ID, "integrateSmeltery", tag);
	}

}

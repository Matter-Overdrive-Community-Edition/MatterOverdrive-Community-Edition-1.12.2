
package matteroverdrive.items.tools;

import matteroverdrive.Reference;
import matteroverdrive.api.internal.ItemModelProvider;
import matteroverdrive.client.ClientUtil;
import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;

/**
 * @author shadowfacts
 */
public class TritaniumSword extends ItemSword implements ItemModelProvider {

	public TritaniumSword(String name) {
		super(MatterOverdriveItems.TOOL_MATERIAL_TRITANIUM);
		setTranslationKey(Reference.MOD_ID + "." + name);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
	}

	@Override
	public void initItemModel() {
		ClientUtil.registerModel(this, this.getRegistryName().toString());
	}

}

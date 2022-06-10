
package matteroverdrive.client.render.weapons.layers;

import matteroverdrive.client.resources.data.WeaponMetadataSection;
import net.minecraft.item.ItemStack;

public interface IWeaponLayer {
	void renderLayer(WeaponMetadataSection weaponMeta, ItemStack weapon, float ticks);
}

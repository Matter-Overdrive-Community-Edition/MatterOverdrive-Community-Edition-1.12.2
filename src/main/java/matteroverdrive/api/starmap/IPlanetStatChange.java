
package matteroverdrive.api.starmap;

import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

public interface IPlanetStatChange {
	float changeStat(ItemStack stack, Planet planet, PlanetStatType statType, float original);
}

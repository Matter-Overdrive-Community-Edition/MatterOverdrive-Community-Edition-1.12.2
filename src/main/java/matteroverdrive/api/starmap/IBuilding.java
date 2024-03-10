
package matteroverdrive.api.starmap;

import net.minecraft.item.ItemStack;

public interface IBuilding extends IBuildable
{
    BuildingType getType(ItemStack building);
}

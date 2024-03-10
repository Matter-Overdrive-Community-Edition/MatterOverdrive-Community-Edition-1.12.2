
package matteroverdrive.api.starmap;

import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

public interface IShip extends IBuildable
{
    ShipType getType(ItemStack ship);

    void onTravel(ItemStack stack,Planet to);
}

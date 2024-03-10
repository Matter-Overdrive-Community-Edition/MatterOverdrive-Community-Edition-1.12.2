
package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.ShipType;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemScoutShip extends ItemShipAbstract
{
    public ItemScoutShip(String name) {
        super(name);
    }

    @Override
    public boolean canBuild(ItemStack building, Planet planet,List<String> info) {
        return true;
    }

    @Override
    public int getBuildLengthUnscaled(ItemStack building, Planet planet) {
        return 20 * 180;
    }

    @Override
    public ShipType getType(ItemStack ship) {
        return ShipType.SCOUT;
    }

    @Override
    public void onTravel(ItemStack stack, Planet to)
    {

    }
}

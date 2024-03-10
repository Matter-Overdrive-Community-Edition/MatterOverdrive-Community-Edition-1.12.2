
package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.BuildingType;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ShipFactory extends ItemBuildingAbstract {

    public ShipFactory(String name) {
        super(name);
    }

    @Override
    public boolean canBuild(ItemStack building, Planet planet,List<String> info) {
        return true;
    }

    @Override
    public int getBuildLengthUnscaled(ItemStack building, Planet planet)
    {
        return 20 * 400;
    }

    @Override
    public BuildingType getType(ItemStack building) {
        return BuildingType.SHIP_FACTORY;
    }
}

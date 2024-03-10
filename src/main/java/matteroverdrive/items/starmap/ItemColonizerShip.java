
package matteroverdrive.items.starmap;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.starmap.BuildingType;
import matteroverdrive.api.starmap.IBuilding;
import matteroverdrive.api.starmap.ShipType;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemColonizerShip  extends ItemShipAbstract
{

    public ItemColonizerShip(String name) {
        super(name);
    }

    @Override
    public ShipType getType(ItemStack ship) {
        return ShipType.COLONIZER;
    }

    @Override
    public void onTravel(ItemStack shipStack, Planet to)
    {
        UUID owner = getOwnerID(shipStack);
        if (owner != null) {
            ItemStack base = new ItemStack(MatterOverdrive.ITEMS.buildingBase);
            MatterOverdrive.ITEMS.buildingBase.setOwner(base,owner);
            if (to.canBuild((IBuilding) base.getItem(), base,new ArrayList<>())) {
                to.addBuilding(base);
                to.setOwnerUUID(owner);
            }
        }
    }

    @Override
    public boolean canBuild(ItemStack building, Planet planet,List<String> info)
    {
        return planet.hasBuildingType(BuildingType.BASE);
    }

    @Override
    public int getBuildLengthUnscaled(ItemStack building, Planet planet) {
        return 20 * 250;
    }
}

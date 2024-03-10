
package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.BuildingType;
import matteroverdrive.api.starmap.IPlanetStatChange;
import matteroverdrive.api.starmap.PlanetStatType;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBuildingShipHangar extends ItemBuildingAbstract implements IPlanetStatChange
{
    private static final int SHIP_SPACES = 2;

    public ItemBuildingShipHangar(String name)
    {
        super(name);
    }

    @Override
    public BuildingType getType(ItemStack building)
    {
        return BuildingType.OTHER;
    }

    @Override
    protected int getBuildLengthUnscaled(ItemStack buildableStack, Planet planet)
    {
        return 20*60*4;
    }

    @Override
    public boolean canBuild(ItemStack building, Planet planet, List<String> info)
    {
        return true;
    }

    @Override
    public float changeStat(ItemStack stack, Planet planet, PlanetStatType statType, float original)
    {
        switch (statType)
        {
            case FLEET_SIZE:
                return original + SHIP_SPACES;
            default:
                return original;
        }
    }
}

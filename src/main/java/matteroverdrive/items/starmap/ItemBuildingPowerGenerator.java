
package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.BuildingType;
import matteroverdrive.api.starmap.IPlanetStatChange;
import matteroverdrive.api.starmap.PlanetStatType;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBuildingPowerGenerator extends ItemBuildingAbstract implements IPlanetStatChange
{
    private static final int POWER_GENERATION = 8;
    private static final int MATTER_CONSUPTION = 2;

    public ItemBuildingPowerGenerator(String name)
    {
        super(name);
    }

    @Override
    public BuildingType getType(ItemStack building)
    {
        return BuildingType.GENERATOR;
    }

    @Override
    protected int getBuildLengthUnscaled(ItemStack buildableStack, Planet planet)
    {
        return 20*60*12;
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
            case ENERGY_PRODUCTION:
                return original + POWER_GENERATION;
            case MATTER_PRODUCTION:
                return original - MATTER_CONSUPTION;
            default:
                return original;
        }
    }
}

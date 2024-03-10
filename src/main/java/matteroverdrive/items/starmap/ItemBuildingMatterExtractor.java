
package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.BuildingType;
import matteroverdrive.api.starmap.IPlanetStatChange;
import matteroverdrive.api.starmap.PlanetStatType;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBuildingMatterExtractor extends ItemBuildingAbstract implements IPlanetStatChange
{
    private static final int MATTER_PRODUCTION = 10;
    private static final int ENERGY_DRAIN = 6;

    public ItemBuildingMatterExtractor(String name)
    {
        super(name);
    }

    @Override
    public BuildingType getType(ItemStack building)
    {
        return BuildingType.ORE_EXTRACTOR;
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
            case MATTER_PRODUCTION:
                return original + MATTER_PRODUCTION;
            case ENERGY_PRODUCTION:
                return original - ENERGY_DRAIN;
            default:
                return original;
        }
    }
}

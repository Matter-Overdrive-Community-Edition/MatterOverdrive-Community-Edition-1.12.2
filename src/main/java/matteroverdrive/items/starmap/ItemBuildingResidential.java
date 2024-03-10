
package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.BuildingType;
import matteroverdrive.api.starmap.IPlanetStatChange;
import matteroverdrive.api.starmap.PlanetStatType;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBuildingResidential extends ItemBuildingAbstract implements IPlanetStatChange
{
    private static final int POPULATION_COUNT = 10000;
    private static final int ENERGY_DRAIN = 4;
    private static final int MATTER_DRAIN = 2;
    private static final int BUILDING_SIZE_INCREASE = 4;

    public ItemBuildingResidential(String name)
    {
        super(name);
    }

    @Override
    public BuildingType getType(ItemStack building)
    {
        return BuildingType.RESIDENTIAL;
    }

    @Override
    protected int getBuildLengthUnscaled(ItemStack buildableStack, Planet planet)
    {
        return 20*60*5;
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
            case POPULATION_COUNT:
                return original + POPULATION_COUNT;
            case ENERGY_PRODUCTION:
                return original - ENERGY_DRAIN;
            case MATTER_PRODUCTION:
                return original - MATTER_DRAIN;
            case BUILDINGS_SIZE:
                return original + BUILDING_SIZE_INCREASE;
            case HAPPINESS:
                return original + calculateHappiness(stack,planet);
            default:
                return original;
        }
    }

    public float calculateHappiness(ItemStack stack, Planet planet)
    {
        float happiness = 0;
        happiness += planet.getPowerProducation() >= 0 ? 0.5f : -0.4f;
        float matterProdruction = planet.getMatterProduction();
        happiness += matterProdruction >= 0 ? 0.5f : -0.6f;
        return happiness;
    }
}

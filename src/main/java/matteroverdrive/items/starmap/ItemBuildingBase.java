
package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.BuildingType;
import matteroverdrive.api.starmap.IBuilding;
import matteroverdrive.api.starmap.IPlanetStatChange;
import matteroverdrive.api.starmap.PlanetStatType;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

import javax.annotation.Nullable;

public class ItemBuildingBase extends ItemBuildingAbstract implements IPlanetStatChange
{
    private static final int BUILDING_SIZE_INCREASE = 2;

    public ItemBuildingBase(String name) {
        super(name);
    }

    @Override
    public BuildingType getType(ItemStack building) {
        return BuildingType.BASE;
    }


    public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List infos)
    {
        super.addDetails(itemstack,player, worldIn, infos);
        if (infos.size() >= 2)
        {
            infos.add(1, String.format((String) infos.get(1), BUILDING_SIZE_INCREASE));
        }
    }

    @Override
    public boolean canBuild(ItemStack building, Planet planet,List<String> info) {
        for (ItemStack buildingStack : planet.getBuildings())
        {
            if (buildingStack.getItem() instanceof IBuilding && ((IBuilding)buildingStack.getItem()).getType(buildingStack) == BuildingType.BASE)
            {
                info.add(MOStringHelper.translateToLocal("gui.tooltip.starmap.has_base"));
                return false;
            }
        }
        return true;
    }

    @Override
    public int getBuildLengthUnscaled(ItemStack building, Planet planet) {
        return 20 * 500;
    }

    @Override
    public float changeStat(ItemStack stack, Planet planet, PlanetStatType statType, float original)
    {
        if (statType == PlanetStatType.BUILDINGS_SIZE)
        {
            return original + BUILDING_SIZE_INCREASE;
        }
        return original;
    }
}

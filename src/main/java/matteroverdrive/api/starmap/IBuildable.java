
package matteroverdrive.api.starmap;

import matteroverdrive.starmap.data.Planet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public interface IBuildable
{
    boolean canBuild(ItemStack building, Planet planet, List<String> info);

    int getBuildLength(ItemStack building, Planet planet);

    long getBuildStart(ItemStack building);

    void setBuildStart(ItemStack building, long buildStart);

    boolean isReadyToBuild(World world, ItemStack stack, Planet planet);

    boolean isOwner(ItemStack ship, EntityPlayer player);

    UUID getOwnerID(ItemStack stack);

    void setOwner(ItemStack ship, UUID ownerID);


    default long getRemainingBuildTimeTicks(ItemStack stack, Planet planet, World world)
    {
        return (getBuildStart(stack) + getBuildLength(stack, planet)) - world.getTotalWorldTime();
    }
}

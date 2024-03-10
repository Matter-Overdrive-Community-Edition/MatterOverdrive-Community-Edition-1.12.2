
package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.IBuildable;
import matteroverdrive.util.math.MOMathHelper;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class ItemBuildableAbstract extends MOBaseItem implements IBuildable
{
    public ItemBuildableAbstract(String name)
    {
        super(name);
    }

    protected abstract int getBuildLengthUnscaled(ItemStack buildableStack, Planet planet);

    @Override
    public long getBuildStart(ItemStack building)
    {
        if (building.hasTagCompound())
        {
            return building.getTagCompound().getLong("BuildStart");
        }
        else
        {
            return 0;
        }
    }

    @Override
    public void setBuildStart(ItemStack building,long buildStart)
    {
        if (!building.hasTagCompound())
        {
            building.setTagCompound(new NBTTagCompound());
        }

        building.getTagCompound().setLong("BuildStart", buildStart);
    }

    @Override
    public boolean isReadyToBuild(World world,ItemStack stack,Planet planet)
    {
        if (stack.hasTagCompound())
        {
            if (getBuildStart(stack) + getBuildLength(stack,planet) < world.getTotalWorldTime())
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isOwner(ItemStack ship, EntityPlayer player)
    {
        if (ship.hasTagCompound())
        {
            if (ship.getTagCompound().hasKey("Owner") && !ship.getTagCompound().getString("Owner").isEmpty()) {
                try {
                    return false; //UUID.fromString(ship.getTagCompound().getString("Owner")).equals(EntityPlayer.func_146094_a(player.getGameProfile()));
                }
                catch (Exception e)
                {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public UUID getOwnerID(ItemStack stack)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Owner",8))
        {
            try {
                return UUID.fromString(stack.getTagCompound().getString("Owner"));
            }catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }

    @Override
    public void setOwner(ItemStack ship, UUID playerId)
    {
        if (!ship.hasTagCompound())
        {
            ship.setTagCompound(new NBTTagCompound());
        }

        ship.getTagCompound().setString("Owner",playerId.toString());
    }

    @Override
    public int getBuildLength(ItemStack buildableStack, Planet planet)
    {
        return 1;//return MathHelper.ceiling_double_int(getBuildLengthUnscaled(buildableStack, planet) * Galaxy.GALAXY_BUILD_TIME_MULTIPLY);
    }
}

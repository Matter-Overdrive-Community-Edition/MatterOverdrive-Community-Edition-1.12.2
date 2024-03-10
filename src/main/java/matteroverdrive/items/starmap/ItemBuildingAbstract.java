
package matteroverdrive.items.starmap;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.starmap.IBuilding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

import javax.annotation.Nullable;

public abstract class ItemBuildingAbstract extends ItemBuildableAbstract implements IBuilding {

    public ItemBuildingAbstract(String name)
    {
        super(name);
        setMaxStackSize(1);
        setCreativeTab(MatterOverdrive.TAB_OVERDRIVE_BUILDINGS);
    }

    public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List infos)
    {
        super.addDetails(itemstack,player,worldIn, infos);
    }

    public boolean hasDetails(ItemStack stack){return true;}
}

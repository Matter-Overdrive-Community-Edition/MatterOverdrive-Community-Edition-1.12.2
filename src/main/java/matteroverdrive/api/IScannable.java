
package matteroverdrive.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 5/11/2015.
 * Used for scannable blocks or tile entities.
 */
public interface IScannable {
    /**
     * Used to add info to the Holo Screen.
     *
     * @param world the world.
     * @param x     the X inate of the scannable target.
     * @param y     the Y inate of the scannable target.
     * @param z     the Z inate of the scannable target.
     * @param infos the info lines list.
     *              Here is where you add the info lines you want displayed.
     */
    void addInfo(World world, double x, double y, double z, List<String> infos);

    /**
     * Called when the target is scanned.
     * Once the scanning process is complete.
     *
     * @param world   the world.
     * @param x       the X inates of the scanned target.
     * @param y       the Y inates of the scanned target.
     * @param z       the Z inates of the scanned target.
     * @param player  the player who scanned the target.
     * @param scanner the scanner item stack.
     */
    void onScan(World world, double x, double y, double z, EntityPlayer player, ItemStack scanner);
}

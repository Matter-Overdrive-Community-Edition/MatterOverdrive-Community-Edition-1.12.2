
package matteroverdrive.handler;

import matteroverdrive.api.network.IMatterNetworkHandler;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOLog;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Level;

public class MatterNetworkTickHandler implements IConfigSubscriber {
    int id_count;
    int last_ID;
    private int max_broadcasts;
    private int broadcastCount;

    public void updateHandler(IMatterNetworkHandler handler, TickEvent.Phase phase, World world) {

        if (broadcastCount < max_broadcasts) {
            if (id_count >= last_ID) {
                try {
                    broadcastCount += handler.onNetworkTick(world, phase);
                } catch (Exception e) {
                    MOLog.log(Level.FATAL, e, "There was a problem while ticking MatterNetworkHandler %s", handler);
                }
            }

            id_count++;
        }
    }

    public void onWorldTickPre(TickEvent.Phase phase, World world) {
        //reset the broadcast counting each tick
        broadcastCount = 0;
        id_count = 0;
    }

    public void onWorldTickPost(TickEvent.Phase phase, World world) {
        if (broadcastCount >= max_broadcasts) {
            //if the broadcast count exceeded the maximum then store the last ID from the ID count.
            //this will restart the broadcasting next tick from the last broadcaster not the beginning.
            last_ID = id_count;
        } else {
            //resets the last ID if broadcast did not exceed the maximum.
            //this will start the broadcasting from beginning next tick.
            last_ID = 0;
        }
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        this.max_broadcasts = config.getInt(ConfigurationHandler.KEY_MAX_BROADCASTS, ConfigurationHandler.CATEGORY_MATTER_NETWORK, 128, "The maximum amount of network packet broadcasts per tick.");
    }
}

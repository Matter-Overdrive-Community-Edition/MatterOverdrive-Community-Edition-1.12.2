
package matteroverdrive.tile;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public interface IMOTickable {
    void onServerTick(TickEvent.Phase phase, World world);
}

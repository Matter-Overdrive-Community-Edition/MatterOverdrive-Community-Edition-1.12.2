
package matteroverdrive.api.matter;

import net.minecraft.util.EnumFacing;

/**
 * Created by Simeon on 3/7/2015.
 *
 * @deprecated This is now replaced by Forge Fluid Tanks. As all machines that store matter are Fluid Tanks.
 */
public interface IMatterConnection {
    boolean canConnectFrom(EnumFacing dir);
}

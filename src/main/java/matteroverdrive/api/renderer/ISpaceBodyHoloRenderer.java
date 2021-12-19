
package matteroverdrive.api.renderer;

import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.tile.TileEntityMachineStarMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISpaceBodyHoloRenderer {
    void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks, float viewerDistance);

    void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks, float opacity);

    boolean displayOnZoom(int zoom, SpaceBody spaceBody);

    double getHologramHeight(SpaceBody spaceBody);
}

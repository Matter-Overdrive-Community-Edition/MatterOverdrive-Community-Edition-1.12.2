
package matteroverdrive.api.starmap;

import matteroverdrive.api.renderer.ISpaceBodyHoloRenderer;
import matteroverdrive.starmap.data.SpaceBody;

import java.util.Collection;

public interface IStarmapRenderRegistry {
    boolean registerRenderer(Class<? extends SpaceBody> spaceBodyType, ISpaceBodyHoloRenderer renderer);

    Collection<ISpaceBodyHoloRenderer> getStarmapRendererCollection(Class<? extends SpaceBody> spaceBodyType);
}

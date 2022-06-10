
package matteroverdrive.client.render;

import matteroverdrive.api.events.MOEventRegisterStarmapRenderer;
import matteroverdrive.api.renderer.ISpaceBodyHoloRenderer;
import matteroverdrive.api.starmap.IStarmapRenderRegistry;
import matteroverdrive.starmap.data.SpaceBody;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StarmapRenderRegistry implements IStarmapRenderRegistry {
	final Map<Class<? extends SpaceBody>, Collection<ISpaceBodyHoloRenderer>> map;

	public StarmapRenderRegistry() {
		map = new HashMap<>();
	}

	@Override
	public boolean registerRenderer(Class<? extends SpaceBody> spaceBodyType, ISpaceBodyHoloRenderer renderer) {
		if (!MinecraftForge.EVENT_BUS.post(new MOEventRegisterStarmapRenderer(spaceBodyType, renderer))) {
			Collection<ISpaceBodyHoloRenderer> renderers = map.get(spaceBodyType);
			if (renderers == null) {
				renderers = new ArrayList<>();
				map.put(spaceBodyType, renderers);
			}
			return renderers.add(renderer);
		}
		return false;
	}

	@Override
	public Collection<ISpaceBodyHoloRenderer> getStarmapRendererCollection(Class<? extends SpaceBody> spaceBodyType) {
		return map.get(spaceBodyType);
	}
}

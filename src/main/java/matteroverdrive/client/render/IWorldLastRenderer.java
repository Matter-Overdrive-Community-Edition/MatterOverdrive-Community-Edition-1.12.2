
package matteroverdrive.client.render;

import matteroverdrive.client.RenderHandler;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public interface IWorldLastRenderer {
	void onRenderWorldLast(RenderHandler handler, RenderWorldLastEvent event);
}

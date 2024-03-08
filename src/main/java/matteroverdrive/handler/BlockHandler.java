
package matteroverdrive.handler;

import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.entity.player.OverdriveExtendedProperties;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockHandler {
	@SubscribeEvent
	public void onHarvestDropsEvent(BlockEvent.HarvestDropsEvent event) {
		if (event.getHarvester() != null) {
			OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider
					.GetExtendedCapability(event.getHarvester());
			if (extendedProperties != null) {
				extendedProperties.onEvent(event);
			}
		}
	}

	@SubscribeEvent
	public void onBlockPlaceEvent(BlockEvent.PlaceEvent event) {
		if (event.getPlayer() != null) {
			OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider
					.GetExtendedCapability(event.getPlayer());
			if (extendedProperties != null) {
				extendedProperties.onEvent(event);
			}
		}
	}
}

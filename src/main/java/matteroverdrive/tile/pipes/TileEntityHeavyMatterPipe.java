
package matteroverdrive.tile.pipes;

import matteroverdrive.util.TimeTracker;

public class TileEntityHeavyMatterPipe extends TileEntityMatterPipe {
	public TileEntityHeavyMatterPipe() {
		t = new TimeTracker();
		storage.setCapacity(128);
		storage.setMaxExtract(128);
		storage.setMaxReceive(128);
		transferSpeed = 5;
	}
}

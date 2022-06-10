
package matteroverdrive.api.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

public class MOEventScan extends PlayerEvent {
	public final ItemStack scannerStack;
	public final RayTraceResult position;
	private final Side side;

	public MOEventScan(EntityPlayer player, ItemStack scannedStack, RayTraceResult position) {
		super(player);
		if (player.world.isRemote) {
			side = Side.CLIENT;
		} else {
			side = Side.SERVER;
		}
		this.scannerStack = scannedStack;
		this.position = position;
	}

	public Side getSide() {
		return side;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}
}

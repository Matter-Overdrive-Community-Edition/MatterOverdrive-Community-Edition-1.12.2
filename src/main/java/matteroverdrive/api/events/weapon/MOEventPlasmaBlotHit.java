
package matteroverdrive.api.events.weapon;

import matteroverdrive.entity.weapon.PlasmaBolt;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Simeon on 7/21/2015. Triggered when a Plasma bolt hits a target.
 * It can be either a block or an Entity.
 */
public class MOEventPlasmaBlotHit extends Event {
	public final ItemStack weapon;
	public final RayTraceResult hit;
	public final PlasmaBolt plasmaBolt;
	public final Side side;

	public MOEventPlasmaBlotHit(ItemStack weapon, RayTraceResult hit, PlasmaBolt plasmaBolt, Side side) {
		this.weapon = weapon;
		this.hit = hit;
		this.plasmaBolt = plasmaBolt;
		this.side = side;
	}
}

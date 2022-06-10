
package matteroverdrive.data;

import net.minecraft.entity.player.EntityPlayer;

public interface IUsableCondition {
	boolean usableByPlayer(EntityPlayer player);
}

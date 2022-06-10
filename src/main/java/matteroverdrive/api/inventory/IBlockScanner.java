
package matteroverdrive.api.inventory;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

public interface IBlockScanner {
	RayTraceResult getScanningPos(ItemStack itemStack, EntityLivingBase player);

	boolean destroysBlocks(ItemStack itemStack);

	boolean showsGravitationalWaves(ItemStack itemStack);
}

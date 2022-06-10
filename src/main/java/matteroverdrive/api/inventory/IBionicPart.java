
package matteroverdrive.api.inventory;

import com.google.common.collect.Multimap;
import matteroverdrive.entity.android_player.AndroidPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 5/26/2015. This class represents parts that can be worn
 * by Android players. By equipping them in the Android Station.
 */
public interface IBionicPart {
	/**
	 * The type of part. At witch part for the body can the Bionic part be worn.
	 * <ol>
	 * <li>Head</li>
	 * <li>Arms</li>
	 * <li>Legs</li>
	 * <li>Chest</li>
	 * <li>Other</li>
	 * <li>Battery</li>
	 * </ol>
	 *
	 * @param itemStack The bionic Items Stack.
	 * @return The type of bionic part.
	 */
	int getType(ItemStack itemStack);

	/**
	 * @param player    The android player.
	 * @param itemStack The bionic item stack.
	 * @return Does the bionic part affect the android player.
	 */
	boolean affectAndroid(AndroidPlayer player, ItemStack itemStack);

	/**
	 * A Multimap of modifiers similar to vanilla armor modifiers.
	 *
	 * @param player    The android player.
	 * @param itemStack The Bionic part item stack.
	 * @return A multimap of modifiers.
	 */
	Multimap<String, AttributeModifier> getModifiers(AndroidPlayer player, ItemStack itemStack);

	/**
	 * Returns the bionic part texture for the given item stack.
	 *
	 * @param androidPlayer the android player.
	 * @param itemStack     the item stack.
	 * @return the resource location of the texture.
	 */
	@SideOnly(Side.CLIENT)
	ResourceLocation getTexture(AndroidPlayer androidPlayer, ItemStack itemStack);

	@SideOnly(Side.CLIENT)
	ModelBiped getModel(AndroidPlayer androidPlayer, ItemStack itemStack);
}

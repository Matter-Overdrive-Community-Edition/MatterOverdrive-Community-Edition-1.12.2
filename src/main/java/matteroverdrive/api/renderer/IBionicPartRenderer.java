
package matteroverdrive.api.renderer;

import matteroverdrive.entity.android_player.AndroidPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 9/10/2015.
 * Used by Bionic parts to handle special rendering.
 * This is used in the
 */
public interface IBionicPartRenderer {
    /**
     * Called when the part is to be rendered
     *
     * @param partStack
     * @param androidPlayer
     * @param renderPlayer
     * @param ticks
     */
    void renderPart(ItemStack partStack, AndroidPlayer androidPlayer, RenderPlayer renderPlayer, float ticks);

    void affectPlayerRenderer(ItemStack partStack, AndroidPlayer androidPlayer, RenderPlayer renderPlayer, float ticks);
}

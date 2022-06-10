
package matteroverdrive.api.android;

import matteroverdrive.api.renderer.IBioticStatRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;

/**
 * Created by Simeon on 7/24/2015. Primary implementation is in
 * {@link matteroverdrive.client.render.AndroidStatRenderRegistry}
 */
@SideOnly(Side.CLIENT)
public interface IAndroidStatRenderRegistry {
	/**
	 * Gets a Collection of all registered Render Handlers for a given type of
	 * Bionic stat.
	 *
	 * @param stat The Class (type) of Bionic stat.
	 * @return The collection of Render Handlers
	 *         ({@link matteroverdrive.api.renderer.IBioticStatRenderer}) for the
	 *         given stat. Returns a Null if there are no registered Render
	 *         Handlers.
	 * @see matteroverdrive.api.renderer.IBioticStatRenderer
	 */
	Collection<IBioticStatRenderer> getRendererCollection(Class<? extends IBioticStat> stat);

	/**
	 * Removes and returns all renderers assigned to that bionic stat (android
	 * ability).
	 *
	 * @param stat the class/type of bionic stat (android ability).
	 * @return a collection of all assigned renderers to that stat class/type.
	 */
	Collection<IBioticStatRenderer> removeAllRenderersFor(Class<? extends IBioticStat> stat);

	/**
	 * Registers a Render Handler of a given BionicStat.
	 *
	 * @param stat     The Class of Bionic Stat.
	 * @param renderer The Render Handler.
	 * @return Did the Render Handler register.
	 */
	boolean registerRenderer(Class<? extends IBioticStat> stat, IBioticStatRenderer renderer);
}

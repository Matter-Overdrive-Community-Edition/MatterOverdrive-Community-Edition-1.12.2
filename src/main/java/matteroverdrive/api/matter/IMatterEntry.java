
package matteroverdrive.api.matter;

import matteroverdrive.data.matter.IMatterEntryHandler;

/**
 * Created by Simeon on 7/21/2015. All matter values for items stored in
 * instances of this class. Used in the {@link IMatterRegistry} to store matter
 * values on items.
 */
public interface IMatterEntry<KEY, MAT> {
	/**
	 * The amount of matter the entry is composed of.
	 *
	 * @return The matter amount of the entry.
	 */
	int getMatter(MAT obj);

	void addHandler(IMatterEntryHandler<MAT> handler);
}

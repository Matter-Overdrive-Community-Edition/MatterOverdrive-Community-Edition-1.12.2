
package matteroverdrive.api.android;

public interface IAndroidStatRegistry {
	/**
	 * Used to register Bionic stats in the Matter overdrive Registry
	 *
	 * @param stat the stat itself
	 * @return if the stat was registered
	 */
	boolean registerStat(IBioticStat stat);

	/**
	 * Gets a specific stat by it's name from Matter Overdrive Bionic Stat Registry
	 *
	 * @param name the name of the Bionic stat to search for
	 * @return The Bionic stat. Null if Bionic stat was not found.
	 */
	IBioticStat getStat(String name);

	/**
	 * Check if a Bionic Stat with a given name exists in the Matter Overdrive
	 * Bionic Stat Registry
	 *
	 * @param name the name of the Bionic Stat
	 * @return True if stat exists. False if it does not.
	 */
	boolean hasStat(String name);

	/**
	 * Unregister (remove) a Bionic stat with a given name from the Matter Overdrive
	 * Bionic Stat Registry Original Matter Overdrive Bionic Stats can also be
	 * removed
	 *
	 * @param name the name of the stat
	 * @return the stat that was removed. Null if there was no stat with the given
	 *         name.
	 */
	IBioticStat unregisterStat(String name);
}

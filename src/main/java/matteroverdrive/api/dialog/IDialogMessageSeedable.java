
package matteroverdrive.api.dialog;

/**
 * Created by Simeon on 8/10/2015.
 * Used by Dialog Messages that need a new random options each time the conversation has started.
 */
public interface IDialogMessageSeedable {
    /**
     * Sets the seed.
     *
     * @param seed The seed.
     */
    void setSeed(long seed);
}

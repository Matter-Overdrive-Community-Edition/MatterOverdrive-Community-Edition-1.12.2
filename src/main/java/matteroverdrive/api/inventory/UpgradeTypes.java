
package matteroverdrive.api.inventory;

/**
 * Created by Simeon on 4/11/2015.
 * All the stat that machines have, and can be changed by {@link IUpgrade}.
 */
public enum UpgradeTypes {
    /**
     * The speed of the Machine
     */
    Speed,
    /**
     * The Power Usage of the Machine
     */
    PowerUsage,
    /**
     * The Main Output of the Machine
     */
    Output,
    /**
     * The Secondary output of the machine
     */
    SecondOutput,
    /**
     * The fail rate of the machine
     */
    Fail,
    /**
     * The range of the Machine
     */
    Range,
    /**
     * The Power Storage of the machine
     */
    PowerStorage,
    /**
     * The Power Transfer speed of the machine
     */
    PowerTransfer,
    /**
     * The Matter Storage of the machine
     */
    MatterStorage,
    /**
     * The Matter Transfer Speed of the machine
     */
    MatterTransfer,
    /**
     * The Matter Usage of the machine
     */
    MatterUsage,
    Other,
    /**
     * To silence a machine
     */
    Muffler
}

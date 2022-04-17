
package matteroverdrive.api.network;

import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Simeon on 4/20/2015.
 * This is used by Machines that can issue tasks (orders) to other Machines on the Matter Network.
 */
public interface IMatterNetworkDispatcher {
    /**
     * Gets the Task queue of the Machine at the given ID.
     *
     * @param queueID the ID of the Queue.
     * @return the task queue at the given ID.
     */
    MatterNetworkTaskQueue<?> getTaskQueue(int queueID);

    /**
     * @return the number of task queues in the machine.
     */
    int getTaskQueueCount();

    BlockPos getPosition();
}

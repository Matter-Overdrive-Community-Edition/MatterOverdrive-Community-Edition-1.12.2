
package matteroverdrive.matter_network;

import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MatterNetworkRegistry {
    public static final List<Class> taskTypes = new ArrayList<>();

    public static int registerTask(Class<? extends MatterNetworkTask> taskClass) {
        taskTypes.add(taskClass);
        return taskTypes.size() - 1;
    }

    public static void register() {
        registerTask(MatterNetworkTaskReplicatePattern.class);
        registerTask(MatterNetworkTaskStorePattern.class);
    }

    public static int getTaskID(Class<? extends MatterNetworkTask> type) throws NoSuchElementException {
        for (int i = 0; i < taskTypes.size(); i++) {
            if (type.equals(taskTypes.get(i))) {
                return i;
            }
        }
        throw new NoSuchElementException(String.format("Task %s was not registered", type));
    }

    public static Class getTaskClass(int id) {
        return taskTypes.get(id);
    }
}

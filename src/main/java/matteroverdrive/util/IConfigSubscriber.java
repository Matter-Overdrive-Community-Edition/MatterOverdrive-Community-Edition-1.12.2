
package matteroverdrive.util;

import matteroverdrive.handler.ConfigurationHandler;

public interface IConfigSubscriber {
    void onConfigChanged(ConfigurationHandler config);
}


package matteroverdrive.api.matter_network;

import matteroverdrive.data.matter_network.IMatterNetworkEvent;

public interface IMatterNetworkComponent {
    void onNetworkEvent(IMatterNetworkEvent event);
}

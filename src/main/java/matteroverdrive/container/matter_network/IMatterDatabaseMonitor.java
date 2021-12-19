
package matteroverdrive.container.matter_network;

import matteroverdrive.api.matter.IMatterDatabase;

import java.util.List;

public interface IMatterDatabaseMonitor {
    List<IMatterDatabase> getConnectedDatabases();
}

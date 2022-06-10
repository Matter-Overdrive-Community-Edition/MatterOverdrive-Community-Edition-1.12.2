
package matteroverdrive.machines.configs;

import java.util.Map;

public interface IConfigurable {
	Map<String, IConfigProperty> getValues();

	IConfigProperty getProperty(String name);
}

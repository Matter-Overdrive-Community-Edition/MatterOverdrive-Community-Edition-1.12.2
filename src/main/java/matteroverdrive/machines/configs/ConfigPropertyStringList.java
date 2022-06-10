
package matteroverdrive.machines.configs;

public class ConfigPropertyStringList extends ConfigPropertyInteger {
	private final String[] names;

	public ConfigPropertyStringList(String key, String unlocalizedName, String[] names, int def) {
		super(key, unlocalizedName, 0, names.length - 1, def);
		this.names = names;
	}

	@Override
	public Class getType() {
		return Enum.class;
	}

	public String[] getList() {
		return names;
	}
}

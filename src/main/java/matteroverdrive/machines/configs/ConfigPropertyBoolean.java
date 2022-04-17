
package matteroverdrive.machines.configs;

import net.minecraft.nbt.NBTTagCompound;

public class ConfigPropertyBoolean extends ConfigPropertyAbstract {

    private boolean value;

    public ConfigPropertyBoolean(String key, String unlocalizedName, boolean def) {
        super(key, unlocalizedName);
        value = def;
    }

    public ConfigPropertyBoolean(String name, String unlocalizedName) {
        super(name, unlocalizedName);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (boolean) value;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean(getUnlocalizedName(), value);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        value = nbt.getBoolean(getUnlocalizedName());
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }
}

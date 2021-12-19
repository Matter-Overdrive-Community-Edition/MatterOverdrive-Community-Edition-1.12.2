
package matteroverdrive.machines.configs;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @autor Simeon
 * @since 8/16/2015
 */
public interface IConfigProperty {
    String getKey();

    String getUnlocalizedName();

    Object getValue();

    void setValue(Object value);

    void writeToNBT(NBTTagCompound nbt);

    void readFromNBT(NBTTagCompound nbt);

    Class getType();
}

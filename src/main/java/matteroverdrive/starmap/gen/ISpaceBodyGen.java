
package matteroverdrive.starmap.gen;

import matteroverdrive.starmap.data.SpaceBody;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

public interface ISpaceBodyGen<T extends SpaceBody> {
    void generateSpaceBody(T body, Random random);

    boolean generateMissing(NBTTagCompound tagCompound, T body, Random random);

    double getWeight(T body);
}

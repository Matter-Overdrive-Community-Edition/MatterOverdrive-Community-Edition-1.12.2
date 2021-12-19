
package matteroverdrive.api.weapon;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 4/14/2015.
 * Used by weapon modules.
 */
public interface IWeaponModule {
    /**
     * Shows the slot type the module can go into.
     *
     * @param module the module stack.
     * @return the slot type the module can go into.
     */
    int getSlot(ItemStack module);

    /**
     * Gets the module's model path.
     *
     * @return the path of the model resource file.
     */
    String getModelPath();

    /**
     * Gets the texture location for the module's model.
     *
     * @param module the module.
     * @return the location of the model texture.
     */
    ResourceLocation getModelTexture(ItemStack module);

    /**
     * Gets the name of the object inside the model.
     * This is used to enable modules to share a base model file.
     *
     * @param module the module.
     * @return the name of the object in the model.
     */
    String getModelName(ItemStack module);

    /**
     * Modifies the give weapon stats.
     *
     * @param statID       the Stat type ID;
     * @param module       the module stack.
     * @param weapon       the weapon stack.
     * @param originalStat the original stat value.
     * @return general value provided by the module.
     */
    float modifyWeaponStat(IWeaponStat statID, ItemStack module, ItemStack weapon, float originalStat);
}


package matteroverdrive.api;

/**
 * IMC message names for Matter Overdrive.
 * <b>Any changes other than additions are strictly forbidden.</b>
 * <pre><code><br>NBTTagCompound data = new NBTTagCompound();
 * NBTTagCompound itemNBT = new NBTTagCompound();
 * data.setTag("Main",new ItemStack(Items.carrot).writeToNBT(itemNBT));
 * itemNBT = new NBTTagCompound();
 * data.setTag("Sec",new ItemStack(Items.gold_nugget).writeToNBT(itemNBT));
 * itemNBT = new NBTTagCompound();
 * data.setTag("Output",new ItemStack(Items.golden_carrot).writeToNBT(itemNBT));
 * data.setInteger("Energy",16000);
 * data.setInteger("Time",200);
 * FMLInterModComms.sendMessage("mo",IMC.INSCRIBER_RECIPE,data);
 *
 * data = new NBTTagCompound();
 * itemNBT = new NBTTagCompound();
 * data.setTag("Item",new ItemStack(Items.carrot).writeToNBT(itemNBT));
 * data.setInteger("Matter",666);
 * FMLInterModComms.sendMessage("mo",IMC.MATTER_REGISTER,data);
 *
 * FMLInterModComms.sendMessage("mo",IMC.MATTER_REGISTRY_BLACKLIST,new ItemStack(Items.carrot));
 *
 * FMLInterModComms.sendMessage("mo",IMC.MATTER_REGISTRY_BLACKLIST_MOD,"modeID");</code></pre>
 *
 * @author shadowfacts
 */
public interface IMC {

    /**
     * Adds the specified ItemStack to the Matter Registry blacklist
     */
    String MATTER_REGISTRY_BLACKLIST = "registry:blacklist:add";

    String MATTER_REGISTRY_BLACKLIST_MOD = "registry:blacklist:mod:add";

    String MATTER_REGISTER = "registry:matter:add";

    String INSCRIBER_RECIPE = "registry:inscriber:recipe:add";
}

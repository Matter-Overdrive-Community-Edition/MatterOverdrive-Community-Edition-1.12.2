package matteroverdrive.tile;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.InscriberSlot;
import matteroverdrive.data.inventory.RemoveOnlySlot;
import matteroverdrive.data.recipes.InscriberRecipe;
import matteroverdrive.init.MatterOverdriveRecipes;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Optional;

public class TileEntityInscriber extends MOTileEntityMachineEnergy {
    private static final EnumSet<UpgradeTypes> upgradeTypes = EnumSet.of(UpgradeTypes.PowerUsage, UpgradeTypes.Speed, UpgradeTypes.PowerStorage, UpgradeTypes.PowerTransfer, UpgradeTypes.Muffler);
    public static int MAIN_INPUT_SLOT_ID, SEC_INPUT_SLOT_ID, OUTPUT_SLOT_ID;
    @SideOnly(Side.CLIENT)
    private float nextHeadX, nextHeadY;
    @SideOnly(Side.CLIENT)
    private float lastHeadX, lastHeadY;
    @SideOnly(Side.CLIENT)
    private float headAnimationTime;
    private int inscribeTime;
    private InscriberRecipe cachedRecipe;

    public TileEntityInscriber() {
        super(4);
        energyStorage.setCapacity(512000);
        energyStorage.setMaxExtract(256);
        energyStorage.setMaxReceive(512000);
        playerSlotsHotbar = true;
        playerSlotsMain = true;
    }

    @Override
    protected void RegisterSlots(Inventory inventory) {
        MAIN_INPUT_SLOT_ID = inventory.AddSlot(new InscriberSlot(true, false).setSendToClient(true));
        SEC_INPUT_SLOT_ID = inventory.AddSlot(new InscriberSlot(true, true));
        OUTPUT_SLOT_ID = inventory.AddSlot(new RemoveOnlySlot(false).setSendToClient(true));
        super.RegisterSlots(inventory);
    }

    protected void manageInscription() {
        if (!world.isRemote) {
            if (this.isInscribing()) {
                if (this.energyStorage.getEnergyStored() >= getEnergyDrainPerTick()) {
                    this.inscribeTime++;
                    energyStorage.modifyEnergyStored(-getEnergyDrainPerTick());
                    UpdateClientPower();

                    if (this.inscribeTime >= getSpeed()) {
                        this.inscribeTime = 0;
                        this.inscribeItem();
                    }
                }
            }
        }

        if (!this.isInscribing()) {
            this.inscribeTime = 0;
        }
    }

    public boolean canPutInOutput() {
        ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT_ID);
        return outputStack.isEmpty() || (cachedRecipe != null && outputStack.isItemEqual(cachedRecipe.getOutput(this)));
    }

    public void inscribeItem() {
        if (cachedRecipe != null && canPutInOutput()) {
            ItemStack outputSlot = inventory.getStackInSlot(OUTPUT_SLOT_ID);
            if (!outputSlot.isEmpty()) {
                outputSlot.grow(1);
            } else {
                inventory.setInventorySlotContents(OUTPUT_SLOT_ID, cachedRecipe.getOutput(this));
            }

            inventory.decrStackSize(MAIN_INPUT_SLOT_ID, 1);
            inventory.decrStackSize(SEC_INPUT_SLOT_ID, 1);

            calculateRecipe();
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        super.writeCustomNBT(nbt, categories, toDisk);
        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setInteger("inscribeTime", inscribeTime);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        super.readCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA)) {
            inscribeTime = nbt.getInteger("inscribeTime");
        }
    }

    @Override
    public boolean getServerActive() {
        return isInscribing() && this.energyStorage.getEnergyStored() >= getEnergyDrainPerTick();
    }

    public int getEnergyDrainPerTick() {
        int maxEnergy = getEnergyDrainMax();
        int speed = getSpeed();
        if (speed > 0) {
            return maxEnergy / speed;
        }
        return 0;
    }

    public int getEnergyDrainMax() {
        if (cachedRecipe != null) {
            return (int) (cachedRecipe.getEnergy() * getUpgradeMultiply(UpgradeTypes.PowerUsage));
        }
        return 0;
    }

    public int getSpeed() {
        if (cachedRecipe != null) {
            return (int) (cachedRecipe.getTime() * getUpgradeMultiply(UpgradeTypes.Speed));
        }
        return 0;
    }

    public boolean isInscribing() {
        return cachedRecipe != null && canPutInOutput();
    }

    @Override
    public SoundEvent getSound() {
        return MatterOverdriveSounds.machine;
    }

    @Override
    public boolean hasSound() {
        return true;
    }

    @Override
    public float soundVolume() {
        ItemStack stack = this.getStackInSlot(MAIN_INPUT_SLOT_ID);

        if (getUpgradeMultiply(UpgradeTypes.Muffler) == 2d || stack.isEmpty()) {
            return 0.0f;
        }

        return 1;
    }

    @Override
    public void update() {
        super.update();
        if (world.isRemote && isActive()) {
            handleHeadAnimation();
        }
        manageInscription();
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, EnumFacing side) {
        return slot == OUTPUT_SLOT_ID;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return upgradeTypes.contains(type);
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {
        if (event instanceof MachineEvent.Awake) {
            calculateRecipe();
        }
    }

    @Override
    public float getProgress() {
        float speed = (float) getSpeed();
        if (speed > 0) {
            return (float) (inscribeTime) / speed;
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    protected void handleHeadAnimation() {
        if (headAnimationTime >= 1) {
            lastHeadX = nextHeadX;
            lastHeadY = nextHeadY;
            nextHeadX = MathHelper.clamp((float) random.nextGaussian(), -1, 1);
            nextHeadY = MathHelper.clamp((float) random.nextGaussian(), -1, 1);
            headAnimationTime = 0;
        }

        headAnimationTime += 0.05f;
    }

    @SideOnly(Side.CLIENT)
    public float geatHeadX() {
        return MOMathHelper.Lerp(lastHeadX, nextHeadX, headAnimationTime);
    }

    @SideOnly(Side.CLIENT)
    public float geatHeadY() {
        return MOMathHelper.Lerp(lastHeadY, nextHeadY, headAnimationTime);
    }

    public void calculateRecipe() {
        ItemStack mainStack = inventory.getStackInSlot(MAIN_INPUT_SLOT_ID);
        ItemStack secStack = inventory.getStackInSlot(SEC_INPUT_SLOT_ID);
        if (!mainStack.isEmpty() && !secStack.isEmpty()) {
            Optional<InscriberRecipe> recipe = MatterOverdriveRecipes.INSCRIBER.get(this);
            cachedRecipe = recipe.orElse(null);
            return;
        }
        cachedRecipe = null;
    }


    @Override
    public ItemStack decrStackSize(int slot, int size) {
        ItemStack stack = super.decrStackSize(slot, size);
        calculateRecipe();
        return stack;
    }

    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        super.setInventorySlotContents(slot, itemStack);
        calculateRecipe();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{MAIN_INPUT_SLOT_ID, SEC_INPUT_SLOT_ID, OUTPUT_SLOT_ID};
    }

}

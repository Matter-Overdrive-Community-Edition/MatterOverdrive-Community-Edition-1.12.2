package matteroverdrive.tile;

import matteroverdrive.data.TileEntityInventory;
import matteroverdrive.data.inventory.CrateSlot;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public class TileEntityNewTritaniumCrate extends MOTileEntity implements IInventory, IInteractionObject {
    final TileEntityInventory inventory;

    private int color;

    public TileEntityNewTritaniumCrate() {
        inventory = new TileEntityInventory(this, MOStringHelper.translateToLocal("container.new_tritanium_crate"));

        for (int i = 0; i < 54; i++) {
            CrateSlot slot = new CrateSlot(false);
            inventory.AddSlot(slot);
        }
    }

    public int getInventorySize() {
        return 54;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.INVENTORY) && toDisk) {
            inventory.writeToNBT(nbt, true);
        }

        if (categories.contains(MachineNBTCategory.COLOR) && toDisk) {
            nbt.setInteger("Color", color);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.COLOR)) {
            color = nbt.getInteger("Color");

            MOLog.info("Setting custom color to: " + color);
        }

        if (categories.contains(MachineNBTCategory.INVENTORY)) {
            inventory.readFromNBT(nbt);
        }
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    public void onAdded(World world, BlockPos pos, IBlockState state) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed(World worldIn, BlockPos pos, IBlockState state) {

    }

    @Override
    public void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock) {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        inventory.writeToNBT(itemStack.getTagCompound(), true);
    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            inventory.readFromNBT(itemStack.getTagCompound());
        }
    }

    public TileEntityInventory getInventory() {
        return inventory;
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inventory.decrStackSize(slot, amount);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return inventory.removeStackFromSlot(index);
    }

    @Override
    public int getField(int id) {
        return inventory.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        inventory.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return inventory.getFieldCount();
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public String getName() {
        return inventory.getName();
    }

    @Override
    public boolean hasCustomName() {
        return inventory.hasCustomName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return inventory.getDisplayName();
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
        inventory.openInventory(entityPlayer);
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
        inventory.closeInventory(entityPlayer);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inventory.isItemValidForSlot(slot, stack);
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, getInventory(), playerIn);
    }

    @Override
    public String getGuiID() {
        return "minecraft:chest";
    }

    public void readInv(NBTTagCompound nbt) {
        NBTTagList invList = nbt.getTagList("inventory", 10);

        for (int i=0; i<invList.tagCount(); i++) {
            NBTTagCompound itemTag = invList.getCompoundTagAt(i);

            int slot = itemTag.getByte("Slot");

            if (slot >= 0 && slot < inventory.getSlots().size()) {
                inventory.setInventorySlotContents(slot, new ItemStack(itemTag));
            }
        }
    }

    public void writeInv(NBTTagCompound nbt, EntityPlayer player) {
        boolean write = false;

        NBTTagList invList = new NBTTagList();

        for (int i=0; i<inventory.getSlots().size(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                continue;
            }

            NBTTagCompound itemTag = new NBTTagCompound();

            itemTag.setByte("Slot", (byte)i);

            player.sendMessage(new TextComponentString("Writing out item: " + inventory.getStackInSlot(i).getDisplayName()));

            inventory.getStackInSlot(i).writeToNBT(itemTag);

            invList.appendTag(itemTag);
        }

        nbt.setTag("inventory", invList);
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}


package matteroverdrive.data.matter;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;

public class MatterEntryItem extends MatterEntryAbstract<Item, ItemStack> {
    public MatterEntryItem() {
        super();
    }

    public MatterEntryItem(Item item) {
        super(item);
    }

    @Override
    public void writeTo(DataOutput output) throws IOException {
        int cachedCount = 0;
        for (IMatterEntryHandler<?> handler : handlers) {
            if (handler instanceof ItemStackHandlerCachable) {
                cachedCount++;
            }
        }

        output.writeInt(cachedCount);

        for (IMatterEntryHandler<?> handler : handlers) {
            if (handler instanceof ItemStackHandlerCachable) {
                ((ItemStackHandlerCachable) handler).writeTo(output);
            }
        }
    }

    @Override
    public void writeTo(NBTTagCompound tagCompound) {
        NBTTagList handlers = new NBTTagList();
        for (IMatterEntryHandler<?> handler : this.handlers) {
            if (handler instanceof ItemStackHandlerCachable) {
                NBTTagCompound handlerTag = new NBTTagCompound();
                ((ItemStackHandlerCachable) handler).writeTo(handlerTag);
                handlers.appendTag(handlerTag);
            }
        }
        tagCompound.setTag("Handlers", handlers);
    }

    @Override
    public void readFrom(DataInput input) throws IOException {
        clearAllCashed();
        int count = input.readInt();
        for (int i = 0; i < count; i++) {
            ItemStackHandlerCachable genericHandler = new ItemStackHandlerCachable();
            genericHandler.readFrom(input);
            handlers.add(genericHandler);
        }
    }

    @Override
    public void readFrom(NBTTagCompound tagCompound) {
        clearAllCashed();
        NBTTagList tagList = tagCompound.getTagList("Handlers", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            ItemStackHandlerCachable genericHandler = new ItemStackHandlerCachable();
            genericHandler.readFrom(tagList.getCompoundTagAt(i));
            handlers.add(genericHandler);
        }
    }

    @Override
    public void readKey(String data) {
        key = Item.getByNameOrId(data);
    }

    @Override
    public String writeKey() {
        return key.getRegistryName().toString();
    }

    @Override
    public boolean hasCached() {
        int cachedCount = 0;
        for (IMatterEntryHandler<?> handler : handlers) {
            if (handler instanceof ItemStackHandlerCachable) {
                cachedCount++;
            }
        }
        return cachedCount > 0;
    }

    public void clearAllCashed() {
        Iterator<IMatterEntryHandler<ItemStack>> handlerIterator = handlers.iterator();
        while (handlerIterator.hasNext()) {
            if (handlerIterator.next() instanceof ItemStackHandlerCachable) {
                handlerIterator.remove();
            }
        }
    }

}


package matteroverdrive.guide;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class MatterOverdriveGuide {
    private static final Map<Integer, MOGuideEntry> entries = new HashMap<>();
    private static final Map<String, GuideCategory> categories = new LinkedHashMap<>();
    private static final Map<String, Class<? extends IGuideElement>> guideElementHandlersMap = new HashMap<>();
    private static int idCounter = 0;

    public static void registerGuideElementHandler(String tag, Class<? extends IGuideElement> guideElementClass) {
        guideElementHandlersMap.put(tag, guideElementClass);
    }

    public static MOGuideEntry registerEntry(MOGuideEntry entry) {
        entries.put(entry.getId(), entry);
        return entry;
    }

    public static MOGuideEntry registerEntry(ItemStack itemStack, int guiX, int guiY) {
        MOGuideEntry entry = new MOGuideEntry(++idCounter, itemStack.getTranslationKey(), itemStack);
        entry.setGuiPos(guiX, guiY);
        registerEntry(entry);
        return entry;
    }

    public static MOGuideEntry registerEntry(Item item, int guiX, int guiY) {
        return registerEntry(new ItemStack(item), guiX, guiY);
    }

    public static MOGuideEntry registerEntry(Block block, int guiX, int guiY) {
        return registerEntry(new ItemStack(block), guiX, guiY);
    }

    public static int getNextFreeID() {
        return idCounter++;
    }

    public static Collection<MOGuideEntry> getGuides() {
        return entries.values();
    }

    public static MOGuideEntry getQuide(int id) {
        return entries.get(id);
    }

    public static MOGuideEntry findGuide(String name) {
        for (MOGuideEntry entry : entries.values()) {
            if (entry.getName().equalsIgnoreCase(name)) {
                return entry;
            }
        }
        return null;
    }

    public static Class<? extends IGuideElement> getElementHandler(String tag) {
        return guideElementHandlersMap.get(tag);
    }

    public static Map<String, GuideCategory> getCategories() {
        return categories;
    }

    public static GuideCategory getCategory(String name) {
        return categories.get(name);
    }

    public static void registerCategory(GuideCategory category) {
        categories.put(category.getName(), category);
    }
}

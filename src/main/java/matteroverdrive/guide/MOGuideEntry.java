
package matteroverdrive.guide;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import matteroverdrive.Reference;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MOGuideEntry {
	public String name;
	private MOGuideEntry parent;
	private ItemStack[] stackIcons;
	@SideOnly(Side.CLIENT)
	private int guiPosX;
	@SideOnly(Side.CLIENT)
	private int guiPosY;
	private List<MOGuideEntry> children;
	private int id;
	private String group;
	@SideOnly(Side.CLIENT)
	private ResourceLocation styleLocation;

	public MOGuideEntry(String name, ItemStack... stackIcons) {
		this.stackIcons = stackIcons;
		this.name = name;
		init();
	}

	public MOGuideEntry(int id, String name, ItemStack... stackIcons) {
		this.stackIcons = stackIcons;
		this.name = name;
		this.id = id;
		init();
	}

	private void init() {
		children = new ArrayList<>();
	}

	public MOGuideEntry setStackIcons(ItemStack... stackIcons) {
		this.stackIcons = stackIcons;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public MOGuideEntry setStackIcons(Item item) {
		NonNullList<ItemStack> stacks = NonNullList.create();
		item.getSubItems(CreativeTabs.SEARCH, stacks);
		if (stacks.size() > 0) {
			this.stackIcons = new ItemStack[stacks.size()];
			this.stackIcons = stacks.toArray(this.stackIcons);
		} else {
			this.stackIcons = new ItemStack[] { new ItemStack(item) };
		}
		return this;
	}

	public String getDisplayName() {
		return MOStringHelper.translateToLocal(String.format("guide.entry.%s.name", name));
	}

	public MOGuideEntry setGuiPos(int x, int y) {
		this.guiPosX = x;
		this.guiPosY = y;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public int getGuiPosX() {
		return guiPosX;
	}

	@SideOnly(Side.CLIENT)
	public int getGuiPosY() {
		return guiPosY;
	}

	public MOGuideEntry getParent() {
		return parent;
	}

	public MOGuideEntry setParent(MOGuideEntry parent) {
		this.parent = parent;
		return this;
	}

	public void addChild(MOGuideEntry child) {
		child.setParent(this);
		children.add(child);
	}

	public ItemStack[] getStackIcons() {
		return stackIcons;
	}

	@SideOnly(Side.CLIENT)
	public MOGuideEntry setStackIcons(Block block) {
		NonNullList<ItemStack> stacks = NonNullList.create();
		block.getSubBlocks(CreativeTabs.SEARCH, stacks);
		if (stacks.size() > 0) {
			this.stackIcons = new ItemStack[stacks.size()];
			this.stackIcons = stacks.toArray(this.stackIcons);
		} else {
			this.stackIcons = new ItemStack[] { new ItemStack(block) };
		}
		return this;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@SideOnly(Side.CLIENT)
	public String getDescription() {
		String info;
		StringWriter writer = new StringWriter();
		InputStream stream;

		try {

			stream = Minecraft.getMinecraft().getResourceManager()
					.getResource(
							new ResourceLocation(getDescriptionPath(Minecraft.getMinecraft().gameSettings.language)))
					.getInputStream();
			info = IOUtils.toString(stream, "UTF-8");
			writer.close();
			stream.close();
		} catch (IOException e) {
			MOLog.warn("Language text %s for entry %s not found.", Minecraft.getMinecraft().gameSettings.language,
					name);
			try {
				stream = Minecraft.getMinecraft().getResourceManager()
						.getResource(new ResourceLocation(getDescriptionPath("en_US"))).getInputStream();
				info = IOUtils.toString(stream, "UTF-8");
				writer.close();
				stream.close();
			} catch (IOException e1) {
				info = "There is no default text entry for " + name;
			}
		}

		return info;
	}

	@SideOnly(Side.CLIENT)
	public InputStream getDescriptionStream() {
		InputStream stream = null;
		try {
			return Minecraft.getMinecraft().getResourceManager()
					.getResource(
							new ResourceLocation(getDescriptionPath(Minecraft.getMinecraft().gameSettings.language)))
					.getInputStream();
		} catch (IOException e) {
			MOLog.warn("Language text %s for entry %s not found.", Minecraft.getMinecraft().gameSettings.language,
					name);
			try {
				return Minecraft.getMinecraft().getResourceManager()
						.getResource(new ResourceLocation(getDescriptionPath("en_US"))).getInputStream();
			} catch (IOException e1) {
				MOLog.warn("Default language entry for %s not found", name);
			}
		}
		return stream;
	}

	public String getDescriptionPath(String language) {
		return String.format("%s%s/%s.xml", Reference.PATH_INFO, language, name);
	}

	public String getGroup() {
		return group;
	}

	public MOGuideEntry setGroup(String group) {
		this.group = group;
		return this;
	}

	public String getName() {
		return name;
	}

	@SideOnly(Side.CLIENT)
	public ResourceLocation getStyleLocation() {
		return styleLocation;
	}

	@SideOnly(Side.CLIENT)
	public void setStyleLocation(ResourceLocation location) {
		this.styleLocation = location;
	}
}


package matteroverdrive.guide;

import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOStringHelper;

import java.util.HashSet;
import java.util.Set;

public class GuideCategory {
    private String name;
    private Set<MOGuideEntry> entries;
    private String holoIcon;

    public GuideCategory(String name) {
        this.name = name;
        entries = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return MOStringHelper.translateToLocal(String.format("guide.category.%s.name", name));
    }

    public void addEntry(MOGuideEntry entry) {
        entries.add(entry);
    }

    public HoloIcon getHoloIcon() {
        return ClientProxy.holoIcons.getIcon(holoIcon);
    }

    public GuideCategory setHoloIcon(String holoIcon) {
        this.holoIcon = holoIcon;
        return this;
    }

    public Set<MOGuideEntry> getEntries() {
        return entries;
    }
}


package matteroverdrive.gui.element.starmap;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.network.packet.server.starmap.PacketStarMapClientCommands;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.StarmapHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementPlanetEntry extends ElementAbstractStarMapEntry<Planet> {
    public ElementPlanetEntry(GuiStarMap gui, ElementGroupList groupList, int width, int height, Planet spaceBody) {
        super(gui, groupList, width, height, spaceBody);
    }

    @Override
    protected void drawElementName(Planet planet, Color color, float multiply) {
        String name = spaceBody.getSpaceBodyName();
        GuiStarMap guiStarMap = (GuiStarMap) gui;
        if (guiStarMap.getMachine().getGalaxyPosition().equals(planet)) {
            name = "@ " + TextFormatting.ITALIC + name;
        }

        StarmapHelper.drawPlanetInfo(planet, name, posX + 16, posY + 10, multiply);
    }

    @Override
    protected Map<HoloIcon, Integer> getIcons(Planet planet) {
        HashMap<HoloIcon, Integer> icons = new HashMap<>();
        HoloIcon shipIcon = ClientProxy.holoIcons.getIcon("icon_shuttle");
        icons.put(shipIcon, 0);

        if (planet.isOwner(Minecraft.getMinecraft().player)) {
            if (planet.isHomeworld()) {
                icons.put(ClientProxy.holoIcons.getIcon("home_icon"), -1);
            }
        }

        return icons;
    }

    @Override
    public void addTooltip(List<String> list, int mouseX, int mouseY) {

    }

    @Override
    protected boolean canTravelTo(Planet planet, EntityPlayer player) {
        return !((GuiStarMap) gui).getMachine().getGalaxyPosition().equals(planet);
    }

    @Override
    protected boolean canView(Planet planet, EntityPlayer player) {
        return !planet.hasOwner() || planet.isOwner(player);
    }

    @Override
    public float getMultiply(Planet planet) {
        GuiStarMap guiStarMap = (GuiStarMap) gui;
        if (guiStarMap.getMachine().getDestination().equals(planet)) {
            return 1;
        } else if (guiStarMap.getMachine().getGalaxyPosition().equals(planet)) {
            return 0.5f;
        }
        return 0.1f;
    }

    @Override
    public ScaleTexture getBG(Planet planet) {
        GuiStarMap guiStarMap = (GuiStarMap) gui;
        if (guiStarMap.getMachine().getGalaxyPosition().equals(planet)) {
            return BG_MIDDLE_DOWN;
        }
        return BG;
    }

    @Override
    protected Color getSpaceBodyColor(Planet planet) {
        return Planet.getGuiColor(planet);
    }

    @Override
    boolean isSelected(Planet planet) {
        return ((GuiStarMap) gui).getMachine().getDestination().equals(planet);
    }

    @Override
    protected void onViewPress() {
        gui.setPage(3);
    }

    @Override
    protected void onTravelPress() {
        TileEntityMachineStarMap starMap = ((GuiStarMap) gui).getMachine();
        MatterOverdrive.NETWORK.sendToServer(new PacketStarMapClientCommands(starMap, starMap.getZoomLevel(), new GalacticPosition(spaceBody), starMap.getDestination()));
    }

    @Override
    protected void onSelectPress() {
        TileEntityMachineStarMap starMap = ((GuiStarMap) gui).getMachine();
        MatterOverdrive.NETWORK.sendToServer(new PacketStarMapClientCommands(starMap, starMap.getZoomLevel(), starMap.getGalaxyPosition(), new GalacticPosition(spaceBody)));
    }
}

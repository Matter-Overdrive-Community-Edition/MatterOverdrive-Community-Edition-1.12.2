
package matteroverdrive.gui.element.starmap;

import matteroverdrive.Reference;
import matteroverdrive.api.starmap.IShip;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class ElementShipEntry extends ElementAbstractStarMapEntry<Planet>
{
    int shipId;
    ItemStack ship;

    public ElementShipEntry(GuiStarMap gui, ElementGroupList groupList, int width, int height,Planet planet, ItemStack ship,int shipId) {
        super(gui, groupList, width, height,planet);
        this.ship = ship;
        this.searchIcon = ClientProxy.holoIcons.getIcon("icon_attack");
        this.shipId = shipId;
    }

    @Override
    protected void drawElementName(Planet planet, Color color, float multiply)
    {
        RenderUtils.renderStack(posX + 10, posY + sizeY / 2 - 8, ship);
        RenderUtils.drawString(Minecraft.getMinecraft().fontRenderer, ship.getDisplayName(), posX + 31, posY + 12, color, multiply);
    }

    @Override
    protected Map<HoloIcon,Integer> getIcons(Planet spaceBody)
    {
        return null;
    }

    @Override
    protected boolean canTravelTo(Planet ship, EntityPlayer player)
    {
        return false;
    }

    @Override
    protected boolean canView(Planet planet,EntityPlayer player)
    {
        if (ship.getItem() instanceof IShip && ((IShip) ship.getItem()).isOwner(ship,Minecraft.getMinecraft().player))
        {
            Planet to = GalaxyClient.getInstance().getTheGalaxy().getPlanet(((GuiStarMap) gui).getMachine().getDestination());
            if (to != null && to != planet) {
                return to.canAddShip(ship, player);
            }
        }
        return false;
    }

    @Override
    public float getMultiply(Planet ship)
    {
        if (groupList.isSelected(this))
        {
            return  1;
        }
        return 0.1f;
    }

    @Override
    protected Color getSpaceBodyColor(Planet planet)
    {
        if (canView(planet,Minecraft.getMinecraft().player)) {
            return Reference.COLOR_HOLO;
        }else
        {
            return Reference.COLOR_HOLO_RED;
        }
    }

    @Override
    boolean isSelected(Planet planet)
    {
        return groupList.isSelected(this);
    }

    @Override
    protected void onViewPress() {
        ((GuiStarMap) gui).getMachine().Attack(((GuiStarMap) gui).getMachine().getGalaxyPosition(), ((GuiStarMap) gui).getMachine().getDestination(), shipId);
        //((GuiStarMap) gui).getMachine().SyncCommandsToServer();
    }

    @Override
    protected void onTravelPress()
    {

    }

    @Override
    protected void onSelectPress() {

        //((GuiStarMap) gui).getMachine().SyncCommandsToServer();
    }
}

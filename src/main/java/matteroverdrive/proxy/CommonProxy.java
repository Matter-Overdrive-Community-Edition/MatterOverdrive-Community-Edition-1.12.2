
package matteroverdrive.proxy;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.compat.MatterOverdriveCompat;
import matteroverdrive.handler.weapon.CommonWeaponHandler;
import matteroverdrive.starmap.GalaxyServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {
	private final CommonWeaponHandler commonWeaponHandler;

	public CommonProxy() {
		commonWeaponHandler = new CommonWeaponHandler();
	}

	public void registerCompatModules() {
		MatterOverdriveCompat.registerModules();
	}

	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().player;
	}

	public void preInit(FMLPreInitializationEvent event) {
		registerCompatModules();
	}

	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(GalaxyServer.getInstance());
		MinecraftForge.EVENT_BUS.register(getWeaponHandler());
		MinecraftForge.EVENT_BUS.register(GalaxyServer.getInstance());
		MatterOverdrive.CONFIG_HANDLER.subscribe(GalaxyServer.getInstance());
		MatterOverdrive.CONFIG_HANDLER.subscribe(GalaxyServer.getInstance().getGalaxyGenerator());
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public CommonWeaponHandler getWeaponHandler() {
		return commonWeaponHandler;
	}

	public boolean hasTranslation(String key) {
		return I18n.canTranslate(key);
	}

	public String translateToLocal(String key, Object... params) {
		return I18n.translateToLocalFormatted(key, params);
	}

	public void matterToast(boolean b, long l) {
	}
}

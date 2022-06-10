
package matteroverdrive.client.render.weapons.modules;

import matteroverdrive.client.render.weapons.WeaponRenderHandler;

public abstract class ModuleRenderAbstract implements IModuleRender {
	protected final WeaponRenderHandler weaponRenderer;

	public ModuleRenderAbstract(WeaponRenderHandler weaponRenderer) {
		this.weaponRenderer = weaponRenderer;
	}
}


package matteroverdrive.client.render.weapons;

import matteroverdrive.Reference;
import net.minecraft.util.ResourceLocation;

public class ItemRenderPlasmaShotgun extends WeaponItemRenderer {
	public static final String TEXTURE = Reference.PATH_ITEM + "plasma_shotgun.png";
	public static final String MODEL = Reference.PATH_MODEL + "item/plasma_shotgun.obj";
	public static final float SCALE = 0.85f;
	public static final float THIRD_PERSON_SCALE = 0.6f;
	public static final float ITEM_SCALE = 0.3f;
	public static final float SCALE_DROP = 0.4f;

	public ItemRenderPlasmaShotgun() {
		super(new ResourceLocation(MODEL));
	}
}

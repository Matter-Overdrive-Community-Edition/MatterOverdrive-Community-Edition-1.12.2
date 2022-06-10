
package matteroverdrive.items.android;

import com.google.common.collect.Multimap;
import matteroverdrive.Reference;
import matteroverdrive.entity.android_player.AndroidAttributes;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class TritaniumSpine extends BionicPart {
	public final UUID healthModifierID = UUID.fromString("208b4d4c-50ef-4b45-a097-4bed633cdbff");
	public final UUID glitchModifierID = UUID.fromString("83e92f1b-12af-4302-98b2-422c16a06c89");

	public TritaniumSpine(String name) {
		super(name);
	}

	@Override
	public int getType(ItemStack itemStack) {
		return 4;
	}

	@Override
	public boolean affectAndroid(AndroidPlayer player, ItemStack itemStack) {
		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> getModifiers(AndroidPlayer player, ItemStack itemStack) {
		Multimap multimap = super.getModifiers(player, itemStack);
		if (multimap.isEmpty()) {
			multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(healthModifierID,
					MOStringHelper.translateToLocal("attribute.name." + SharedMonsterAttributes.MAX_HEALTH.getName()),
					2f, 0));
			multimap.put(AndroidAttributes.attributeGlitchTime.getName(), new AttributeModifier(glitchModifierID,
					MOStringHelper.translateToLocal("attribute.name.android.glitchTime"), -0.5f, 2));
		}
		return multimap;
	}

	@Override
	public ResourceLocation getTexture(AndroidPlayer androidPlayer, ItemStack itemStack) {
		return new ResourceLocation(Reference.PATH_ARMOR + "tritanium_spine.png");
	}

	@Override
	public ModelBiped getModel(AndroidPlayer androidPlayer, ItemStack itemStack) {
		return null;
	}
}

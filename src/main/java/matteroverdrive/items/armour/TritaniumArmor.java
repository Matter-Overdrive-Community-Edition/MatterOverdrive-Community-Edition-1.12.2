
package matteroverdrive.items.armour;

import matteroverdrive.Reference;
import matteroverdrive.api.internal.ItemModelProvider;
import matteroverdrive.client.ClientUtil;
import matteroverdrive.client.model.ModelTritaniumArmor;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TritaniumArmor extends ItemArmor implements ItemModelProvider {
	public TritaniumArmor(String name, ArmorMaterial armorMaterial, int renderIndex, EntityEquipmentSlot slot) {
		super(armorMaterial, renderIndex, slot);
		setTranslationKey(Reference.MOD_ID + "." + name);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return String.format(Reference.PATH_ARMOR + "Tritanium_Armor2_layer_%d.png",
				slot == EntityEquipmentSlot.FEET ? 2 : 1);
		// return String.format(Reference.PATH_ARMOR + "tritanium_layer_%d%s.png",(slot
		// == 2 ? 2 : 1),type == null ? "" : String.format("_%s", type));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack,
			EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
		ModelTritaniumArmor armorModel = armorSlot == EntityEquipmentSlot.FEET
				? ClientProxy.renderHandler.modelTritaniumArmorFeet
				: ClientProxy.renderHandler.modelTritaniumArmor;
		Render<AbstractClientPlayer> render = Minecraft.getMinecraft().getRenderManager()
				.getEntityRenderObject((AbstractClientPlayer) entityLiving);
		if (render instanceof RenderPlayer) {
			armorModel.setModelAttributes(_default);
			// armorModel.setModelAttributes(((RenderPlayer) render).getMainModel());
			// armorModel.setLivingAnimations(entityLiving, p_177182_2_, p_177182_3_,
			// p_177182_4_);
		}
		/*
		 * armorModel.bipedHead.showModel = armorSlot == 0;
		 * armorModel.bipedHeadwear.showModel = armorSlot == 0;
		 * armorModel.bipedBody.showModel = armorSlot == 1;
		 * armorModel.bipedRightArm.showModel = armorSlot == 1;
		 * armorModel.bipedLeftArm.showModel = armorSlot == 1;
		 * armorModel.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
		 * armorModel.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;
		 * armorModel.FootLeft.showModel = armorModel.FootRight.showModel = armorSlot ==
		 * 3;
		 * 
		 * Render render =
		 * Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(
		 * entityLiving);
		 * 
		 * if (render instanceof RenderPlayer) { RenderPlayer renderPlayer =
		 * (RenderPlayer)render; armorModel.isSneak = entityLiving.isSneaking();
		 * armorModel.heldItemRight = renderPlayer.getMainModel().heldItemRight;
		 * armorModel.heldItemLeft = renderPlayer.getMainModel().heldItemLeft;
		 * armorModel.aimedBow = renderPlayer.getMainModel().aimedBow; }
		 */

		return armorModel;
	}

	@Override
	public void initItemModel() {
		ClientUtil.registerModel(this, this.getRegistryName().toString());
	}

}
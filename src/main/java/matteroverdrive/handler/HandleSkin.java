package matteroverdrive.handler;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.HashMultimap;

/*
import flaxbeard.cyberware.Cyberware;
import flaxbeard.cyberware.api.CyberwareAPI;
import flaxbeard.cyberware.api.CyberwareUpdateEvent;
import flaxbeard.cyberware.api.ICyberwareUserData;
import flaxbeard.cyberware.api.item.ICyberware.EnumSlot;
import flaxbeard.cyberware.api.item.ICyberware.ISidedLimb.EnumSide;
import flaxbeard.cyberware.client.ClientUtils;
import flaxbeard.cyberware.common.CyberwareConfig;
import flaxbeard.cyberware.common.CyberwareContent;
import flaxbeard.cyberware.common.block.tile.TileEntitySurgery;
import flaxbeard.cyberware.common.item.ItemCyberlimb;
*/
public class HandleSkin {

	public static final HandleSkin INSTANCE = new HandleSkin();

	private static Map<Integer, Integer> timesLungs = new HashMap<>();

	private static final UUID idMissingLegSpeedAttribute = UUID.fromString("fe00fdea-5044-11e6-beb8-9e71128cae77");
	private static final HashMultimap<String, AttributeModifier> multimapMissingLegSpeedAttribute;

	static {
		multimapMissingLegSpeedAttribute = HashMultimap.create();
		multimapMissingLegSpeedAttribute.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(),
				new AttributeModifier(idMissingLegSpeedAttribute, "Missing leg speed", -100F, 0));
	}

	private Map<Integer, Boolean> last = new HashMap<>();
	private Map<Integer, Boolean> lastClient = new HashMap<>();

	@SubscribeEvent
	public void triggerCyberwareEvent(LivingUpdateEvent event) {
		EntityLivingBase entityLivingBase = event.getEntityLiving();

		// ICyberwareUserData cyberwareUserData =
		// CyberwareAPI.getCapabilityOrNull(entityLivingBase);
		// if (cyberwareUserData != null)
		// {
		// CyberwareUpdateEvent cyberwareUpdateEvent = new
		// CyberwareUpdateEvent(entityLivingBase, cyberwareUserData);
		// MinecraftForge.EVENT_BUS.post(cyberwareUpdateEvent);
		// }
	}

	/*
	 * @SubscribeEvent(priority=EventPriority.LOWEST) public void
	 * handleMissingEssentials(CyberwareUpdateEvent event) { EntityLivingBase
	 * entityLivingBase = event.getEntityLiving(); ICyberwareUserData
	 * cyberwareUserData = event.getCyberwareUserData();
	 * 
	 * if (entityLivingBase.ticksExisted % 20 == 0) {
	 * cyberwareUserData.resetBuffer(); }
	 * 
	 * 
	 * if ( entityLivingBase instanceof EntityPlayer &&
	 * entityLivingBase.ticksExisted % 20 == 0 ) { int tolerance =
	 * cyberwareUserData.getTolerance(entityLivingBase);
	 * 
	 * if (tolerance <= 0) {
	 * 
	 * }
	 * 
	 * }
	 * 
	 * int numMissingLegs = 0; int numMissingLegsVisible = 0;
	 * 
	 * if (entityLivingBase instanceof EntityPlayer) { if (numMissingLegsVisible ==
	 * 2) { entityLivingBase.height = 1.8F - (10F / 16F); ((EntityPlayer)
	 * entityLivingBase).eyeHeight = ((EntityPlayer)
	 * entityLivingBase).getDefaultEyeHeight() - (10F / 16F); AxisAlignedBB
	 * axisalignedbb = entityLivingBase.getEntityBoundingBox();
	 * entityLivingBase.setEntityBoundingBox(new AxisAlignedBB( axisalignedbb.minX,
	 * axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX +
	 * entityLivingBase.width, axisalignedbb.minY + entityLivingBase.height,
	 * axisalignedbb.minZ + entityLivingBase.width));
	 * 
	 * if (entityLivingBase.world.isRemote) {
	 * lastClient.put(entityLivingBase.getEntityId(), true); } else {
	 * last.put(entityLivingBase.getEntityId(), true); } } else if
	 * (last(entityLivingBase.world.isRemote, entityLivingBase)) {
	 * entityLivingBase.height = 1.8F; ((EntityPlayer) entityLivingBase).eyeHeight =
	 * ((EntityPlayer) entityLivingBase).getDefaultEyeHeight(); AxisAlignedBB
	 * axisalignedbb = entityLivingBase.getEntityBoundingBox();
	 * entityLivingBase.setEntityBoundingBox(new AxisAlignedBB( axisalignedbb.minX,
	 * axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX +
	 * entityLivingBase.width, axisalignedbb.minY + entityLivingBase.height,
	 * axisalignedbb.minZ + entityLivingBase.width ));
	 * 
	 * if (entityLivingBase.world.isRemote) {
	 * lastClient.put(entityLivingBase.getEntityId(), false); } else {
	 * last.put(entityLivingBase.getEntityId(), false); } } }
	 * 
	 * if ( numMissingLegs >= 1 && entityLivingBase.onGround ) {
	 * entityLivingBase.getAttributeMap().applyAttributeModifiers(
	 * multimapMissingLegSpeedAttribute); } else if ( numMissingLegs >= 1 ||
	 * entityLivingBase.ticksExisted % 20 == 0 ) {
	 * entityLivingBase.getAttributeMap().removeAttributeModifiers(
	 * multimapMissingLegSpeedAttribute); }
	 * 
	 * else if (entityLivingBase.ticksExisted % 20 == 0) {
	 * timesLungs.remove(entityLivingBase.getEntityId()); } }
	 * 
	 * private boolean last(boolean remote, EntityLivingBase entityLivingBase) { if
	 * (remote) { if (!lastClient.containsKey(entityLivingBase.getEntityId())) {
	 * lastClient.put(entityLivingBase.getEntityId(), false); } return
	 * lastClient.get(entityLivingBase.getEntityId()); } else { if
	 * (!last.containsKey(entityLivingBase.getEntityId())) {
	 * last.put(entityLivingBase.getEntityId(), false); } return
	 * last.get(entityLivingBase.getEntityId()); } }
	 */
}

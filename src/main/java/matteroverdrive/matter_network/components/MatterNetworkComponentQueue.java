
package matteroverdrive.matter_network.components;

import matteroverdrive.data.matter_network.IMatterNetworkEvent;
import matteroverdrive.tile.TileEntityMachinePacketQueue;

public class MatterNetworkComponentQueue extends MatterNetworkComponentClient<TileEntityMachinePacketQueue> {

	public static final int[] directions = { 0, 1, 2, 3, 4, 5 };

	public MatterNetworkComponentQueue(TileEntityMachinePacketQueue queue) {
		super(queue);
	}

	@Override
	public void onNetworkEvent(IMatterNetworkEvent event) {

	}

	/*
	 * @Override public boolean canPreform(MatterNetworkPacket packet) { return
	 * rootClient.getRedstoneActive(); }
	 * 
	 * @Override public void queuePacket(MatterNetworkPacket packet) { if
	 * (canPreform(packet) && packet.isValid(getworld())) { if
	 * (getPacketQueue(0).queue(packet)) { packet.addToPath(rootClient);
	 * packet.tickAlive(getworld(),true);
	 * MatterOverdrive.NETWORK.sendToAllAround(new PacketSendQueueFlash(rootClient),
	 * rootClient, 32); } } }
	 * 
	 * @Override protected void executePacket(MatterNetworkPacket packet) {
	 * 
	 * }
	 */

	/*
	 * protected int handlePacketBroadcast(World world,MatterNetworkPacket packet) {
	 * int broadcastCount = 0; for (int direction : directions) { if
	 * (MatterNetworkHelper.broadcastPacketInDirection(world, packet, rootClient,
	 * EnumFacing.VALUES[direction])) { broadcastCount++; } } return broadcastCount;
	 * }
	 */

	/*
	 * @Override public int onNetworkTick(World world, TickEvent.Phase phase) { int
	 * broadcastCount = 0; if (phase == TickEvent.Phase.END) { for (int i = 0;i <
	 * getPacketQueueCount();i++) { getPacketQueue(i).tickAllAlive(world, true);
	 * 
	 * MatterNetworkPacket packet = getPacketQueue(i).dequeue(); if (packet != null)
	 * { if (packet.isValid(world)) {
	 * 
	 * broadcastCount += handlePacketBroadcast(world, packet); } } } } return
	 * broadcastCount; }
	 */
}

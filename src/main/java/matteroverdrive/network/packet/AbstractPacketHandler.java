package matteroverdrive.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class AbstractPacketHandler<T extends IMessage> implements IMessageHandler<T, IMessage> {
    @SideOnly(Side.CLIENT)
    public abstract void handleClientMessage(EntityPlayerSP player, T message, MessageContext ctx);

    public abstract void handleServerMessage(EntityPlayerMP player, T message, MessageContext ctx);

    @Override
    public IMessage onMessage(T message, MessageContext ctx) {
        final IThreadListener thread = FMLCommonHandler.instance().getWorldThread(ctx.netHandler);
		 if (thread.isCallingFromMinecraftThread()) {
            thread.addScheduledTask(() -> handleClientMessage(Minecraft.getMinecraft().player, message, ctx));
        } else {
            thread.addScheduledTask(() -> handleServerMessage(ctx.getServerHandler().player, message, ctx));
        }
        return null;
    }
}
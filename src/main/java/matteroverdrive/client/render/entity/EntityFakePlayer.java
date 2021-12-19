
package matteroverdrive.client.render.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class EntityFakePlayer extends EntityPlayer {
    public EntityFakePlayer(World world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }

    @Override
    public void sendMessage(ITextComponent chatComponent) {

    }

    @Override
    public boolean canUseCommand(int permLevel, String commandName) {
        return false;
    }
}


package matteroverdrive.client.sound;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class MOPositionedSound extends PositionedSound {
	public MOPositionedSound(SoundEvent event, SoundCategory category, float volume, float pitch) {
		super(event, category);
		this.pitch = pitch;
		this.volume = volume;
	}

	public void setPosition(float x, float y, float z) {
		this.xPosF = x;
		this.yPosF = y;
		this.zPosF = z;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public MOPositionedSound setAttenuationType(ISound.AttenuationType type) {
		attenuationType = type;
		return this;
	}
}


package matteroverdrive.client.sound;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class WeaponSound extends PositionedSound implements ITickableSound {
	private boolean donePlaying;

	public WeaponSound(SoundEvent sound, SoundCategory category, float x, float y, float z, float volume, float pitch) {
		super(sound, category);
		setPosition(x, y, z);
		this.volume = volume;
		this.pitch = pitch;
		this.repeat = true;
		this.repeatDelay = 0;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	@Override
	public boolean isDonePlaying() {
		return donePlaying;
	}

	public void stopPlaying() {
		donePlaying = true;
	}

	public void startPlaying() {
		donePlaying = false;
	}

	public void setPosition(float x, float y, float z) {
		this.xPosF = x;
		this.yPosF = y;
		this.zPosF = z;
	}

	@Override
	public void update() {

	}
}

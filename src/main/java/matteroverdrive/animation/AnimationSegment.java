
package matteroverdrive.animation;

public abstract class AnimationSegment {
	int begin;
	int length;

	public AnimationSegment(int begin, int length) {
		this.begin = begin;
		this.length = length;
	}

	public AnimationSegment(int length) {
		this.length = length;
	}
}

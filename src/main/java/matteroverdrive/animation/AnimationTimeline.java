
package matteroverdrive.animation;

import java.util.ArrayList;
import java.util.List;

import matteroverdrive.util.math.MOMathHelper;

public class AnimationTimeline<T extends AnimationSegment> {
	protected int time;
	private boolean loopable;
	private int duration;
	private List<T> segments;
	private int lastSegmentBegin;

	public AnimationTimeline(boolean loopable, int duration) {
		segments = new ArrayList<>();
		this.loopable = loopable;
		this.duration = duration;
	}

	public double getPercent() {
		return (double) time / (double) duration;
	}

	public void addSegment(T segment) {
		segments.add(segment);
	}

	public void addSegmentSequential(T segment) {
		segment.begin = lastSegmentBegin;
		lastSegmentBegin += segment.length;
		segments.add(segment);
	}

	public T getCurrentSegment() {
		for (T segment : segments) {
			if (MOMathHelper.animationInRange(time, segment.begin, segment.length)) {
				return segment;
			}
		}
		return null;
	}

	public void tick() {
		if (time < duration) {
			time++;
		} else if (loopable) {
			time = 0;
		}
	}

	public void setTime(int time) {
		this.time = time;
	}
}

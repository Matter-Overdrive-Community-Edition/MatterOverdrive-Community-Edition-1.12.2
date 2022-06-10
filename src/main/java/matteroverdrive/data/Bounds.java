
package matteroverdrive.data;

public class Bounds {
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;

	public Bounds(int minX, int minY, int maxX, int maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public void extendMin(int minX, int minY) {
		this.minX = Math.min(minX, this.minX);
		this.minY = Math.min(minY, this.minY);
	}

	public void extendMinBy(int minX, int minY) {
		this.minX += minX;
		this.minY += minY;
	}

	public void extendMax(int maxX, int maxY) {
		this.maxX = Math.max(maxX, this.maxX);
		this.maxY = Math.max(maxY, this.maxY);
	}

	public void extendMaxBy(int maxX, int maxY) {
		this.maxX += maxX;
		this.maxY += maxY;
	}

	public void extend(int minX, int minY, int maxX, int maxY) {
		extendMin(minX, minY);
		extendMax(maxX, maxY);
	}

	public void extend(Bounds bounds) {
		extendMin(bounds.minX, bounds.minY);
		extendMax(bounds.maxX, bounds.maxY);
	}

	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getWidth() {
		return maxX - minX;
	}

	public int getHeight() {
		return maxY - minY;
	}
}

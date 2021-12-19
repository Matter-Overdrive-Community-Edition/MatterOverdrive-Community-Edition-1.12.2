
package matteroverdrive.client.data;

public class Color extends Number {
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color RED = new Color(255, 0, 0);
    public static final Color GREEN = new Color(0, 255, 0);
    public static final Color BLUE = new Color(0, 0, 255);
    private final int color;

    public Color(int color) {
        this.color = color;
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Color(int r, int g, int b, int a) {
        this(b & 255 | (g & 255) << 8 | (r & 255) << 16 | (a & 255) << 24);
    }

    public Color multiplyWithoutAlpha(float multiply) {
        return multiply(multiply, multiply, multiply, 1f);
    }

    public Color multiplyWithAlpha(float multiply) {
        return multiply(multiply, multiply, multiply, multiply);
    }

    public Color multiply(float rm, float gm, float bm, float am) {
        return new Color((int) (getIntR() * rm), (int) (getIntG() * gm), (int) (getIntB() * bm), (int) (getIntA() * am));
    }

    public Color add(Color color) {
        return new Color(getIntR() + color.getIntR(), getIntG() + color.getIntG(), getIntB() + color.getIntB(), getIntA() + color.getIntA());
    }

    public Color subtract(Color color) {
        return new Color(getIntR() - color.getIntR(), getIntG() - color.getIntG(), getIntB() - color.getIntB(), getIntA() - color.getIntA());
    }


    public int getIntR() {
        return this.color >> 16 & 255;
    }

    public int getIntG() {
        return this.color >> 8 & 255;
    }

    public int getIntB() {
        return this.color & 255;
    }

    public int getIntA() {
        return this.color >> 24 & 255;
    }

    public int getColor() {
        return color;
    }


    public float getFloatR() {
        return (float) getIntR() / 255f;
    }

    public float getFloatG() {
        return (float) getIntG() / 255f;
    }

    public float getFloatB() {
        return (float) getIntB() / 255f;
    }

    public float getFloatA() {
        return (float) getIntA() / 255f;
    }

    @Override
    public int intValue() {
        return color;
    }

    @Override
    public long longValue() {
        return (long) color;
    }

    @Override
    public float floatValue() {
        return (float) color;
    }

    @Override
    public double doubleValue() {
        return (double) color;
    }

}

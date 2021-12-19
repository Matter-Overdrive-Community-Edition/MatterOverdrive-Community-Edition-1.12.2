
package matteroverdrive.gui.android;

public enum AndroidHudPosition {
    TOP_LEFT(0, 0, "Top Left"),
    TOP_CENTER(0.5f, 0, "Top Center"),
    TOP_RIGHT(1, 0, "Top Right"),
    MIDDLE_LEFT(0, 0.5f, "Middle Left"),
    MIDDLE_CENTER(0.5f, 0.5f, "Middle Center"),
    MIDDLE_RIGHT(1f, 0.5f, "Middle Right"),
    BOTTOM_LEFT(0, 1, "Bottom Left"),
    BOTTOM_CENTER(0.5f, 1, "Bottom Center"),
    BOTTOM_RIGHT(1, 1, "Bottom Right");

    public final float x;
    public final float y;
    private final String name;

    AndroidHudPosition(float x, float y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public static String[] getNames() {
        String[] names = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            names[i] = values()[i].name;
        }
        return names;
    }
}

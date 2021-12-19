
package matteroverdrive.data.matter;

public abstract class MatterEntryHandlerAbstract<T> implements IMatterEntryHandler<T> {
    protected int priority = Byte.MAX_VALUE;

    @Override
    public int compareTo(IMatterEntryHandler<T> o) {
        return Integer.compare(priority, o.getPriority());
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }
}

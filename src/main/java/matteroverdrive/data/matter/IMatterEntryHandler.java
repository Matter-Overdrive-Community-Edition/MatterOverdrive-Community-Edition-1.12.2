
package matteroverdrive.data.matter;

public interface IMatterEntryHandler<T> extends Comparable<IMatterEntryHandler<T>> {
    int modifyMatter(T obj, int originalMatter);

    int getPriority();

    boolean finalModification(T obj);
}

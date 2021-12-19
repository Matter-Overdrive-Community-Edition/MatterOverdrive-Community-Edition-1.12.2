
package matteroverdrive.multiblock;

public interface IMultiBlockTileStructure {
    boolean addMultiBlockTile(IMultiBlockTile block);

    void removeMultiBlockTile(IMultiBlockTile block);

    boolean containsMultiBlockTile(IMultiBlockTile block);
}


package matteroverdrive.multiblock;

public interface IMultiBlockTile {
	boolean canJoinMultiBlockStructure(IMultiBlockTileStructure structure);

	boolean isTileInvalid();

	IMultiBlockTileStructure getMultiBlockHandler();

	void setMultiBlockTileStructure(IMultiBlockTileStructure structure);
}

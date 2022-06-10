
package matteroverdrive.api.network;

public enum MatterNetworkTaskState {
	INVALID, UNKNOWN, WAITING, QUEUED, PROCESSING, FINISHED;

	public static MatterNetworkTaskState get(int ordinal) {
		return MatterNetworkTaskState.values()[ordinal];
	}

	public boolean below(MatterNetworkTaskState state) {
		return ordinal() < state.ordinal();
	}

	public boolean belowOrEqual(MatterNetworkTaskState state) {
		return ordinal() <= state.ordinal();
	}

	public boolean above(MatterNetworkTaskState state) {
		return ordinal() > state.ordinal();
	}
}

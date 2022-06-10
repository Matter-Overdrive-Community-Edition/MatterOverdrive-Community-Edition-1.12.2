
package matteroverdrive.api.transport;

import java.util.List;

/**
 * Created by Simeon on 5/5/2015. A list of Transform Locations. Used by
 * Transporters.
 */
public interface ITransportList {

	/**
	 * Gets a list of transportable locations.
	 *
	 * @return the list of transport locations.
	 */
	List<TransportLocation> getPositions();

}

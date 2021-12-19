
package matteroverdrive.api.gravity;

/**
 * Created by Simeon on 8/1/2015.
 * Entities that implement this interface can disable gravitational effect on them.
 */
public interface IGravityEntity {
    /**
     * @param anomaly
     * @return is the entity affected by the gravitational anomaly.
     */
    boolean isAffectedByAnomaly(IGravitationalAnomaly anomaly);

    /**
     * Called when the entity is consumed by the anomaly.
     *
     * @param anomaly the anomaly
     */
    void onEntityConsumed(IGravitationalAnomaly anomaly);
}

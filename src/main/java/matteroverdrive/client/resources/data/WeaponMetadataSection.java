
package matteroverdrive.client.resources.data;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class WeaponMetadataSection implements IMetadataSection {
    private final Map<String, Vec3d> modulePositions;

    public WeaponMetadataSection() {
        this.modulePositions = new HashMap<>();
    }

    public Map<String, Vec3d> getModulePositions() {
        return modulePositions;
    }

    public void setModulePosition(String module, Vec3d pos) {
        modulePositions.put(module, pos);
    }

    public Vec3d getModulePosition(String module, Vec3d def) {
        Vec3d moduelPos = modulePositions.get(module);
        if (moduelPos != null) {
            return moduelPos;
        } else {
            return def;
        }
    }

    public Vec3d getModulePosition(String module) {
        return modulePositions.get(module);
    }
}

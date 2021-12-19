
package matteroverdrive.data;

import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class MOAttributeModifier extends AttributeModifier {
    double value;

    public MOAttributeModifier(String name, double value, int operation) {
        super(name, value, operation);
        this.value = value;
    }

    public MOAttributeModifier(UUID uuid, String name, double value, int operation) {
        super(uuid, name, value, operation);
        this.value = value;
    }

    public double getAmount() {
        return this.value;
    }

    public void setAmount(double value) {
        this.value = value;
    }
}


package matteroverdrive.client.resources.data;

import com.google.gson.*;
import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Type;
import java.util.Map;

public class WeaponMetadataSectionSerializer extends BaseMetadataSectionSerializer implements JsonSerializer<WeaponMetadataSection> {
    @Override
    public String getSectionName() {
        return "weapon";
    }

    @Override
    public WeaponMetadataSection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WeaponMetadataSection weaponMetadataSection = new WeaponMetadataSection();
        JsonObject jsonobject = JsonUtils.getJsonObject(json, "metadata section");
        try {
            JsonObject modules = jsonobject.getAsJsonObject("modules");
            if (modules != null) {
                for (Map.Entry<String, JsonElement> element : modules.entrySet()) {
                    weaponMetadataSection.setModulePosition(element.getKey(), fromJson(element.getValue().getAsJsonObject()));
                }
            }
        } catch (ClassCastException classcastexception) {
            throw new JsonParseException("Invalid weapon->scope_position: expected array, was " + jsonobject.get("scope_position"), classcastexception);
        }
        return weaponMetadataSection;
    }

    @Override
    public JsonElement serialize(WeaponMetadataSection section, Type type, JsonSerializationContext context) {
        JsonObject jsonobject = new JsonObject();
        JsonObject modules = new JsonObject();
        for (Map.Entry<String, Vec3d> position : section.getModulePositions().entrySet()) {

            modules.add(position.getKey(), toObject(position.getValue()));
        }
        jsonobject.add("modules", modules);
        return jsonobject;
    }

    public JsonObject toObject(Vec3d vec3) {
        JsonObject object = new JsonObject();
        object.add("x", new JsonPrimitive(vec3.x));
        object.add("y", new JsonPrimitive(vec3.y));
        object.add("z", new JsonPrimitive(vec3.z));
        return object;
    }

    public Vec3d fromJson(JsonObject object) {
        return new Vec3d(object.get("x").getAsDouble(), object.get("y").getAsDouble(), object.get("z").getAsDouble());
    }
}

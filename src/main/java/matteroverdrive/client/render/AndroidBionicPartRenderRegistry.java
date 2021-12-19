
package matteroverdrive.client.render;

import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.api.renderer.IBionicPartRenderRegistry;
import matteroverdrive.api.renderer.IBionicPartRenderer;

import java.util.HashMap;
import java.util.Map;

public class AndroidBionicPartRenderRegistry implements IBionicPartRenderRegistry {
    private final Map<Class<? extends IBionicPart>, IBionicPartRenderer> rendererMap;

    public AndroidBionicPartRenderRegistry() {
        rendererMap = new HashMap<>();
    }

    @Override
    public void register(Class<? extends IBionicPart> partClass, IBionicPartRenderer renderer) {
        rendererMap.put(partClass, renderer);
    }

    @Override
    public IBionicPartRenderer removeRenderer(Class<? extends IBionicPart> partClass) {
        return rendererMap.remove(partClass);
    }

    @Override
    public IBionicPartRenderer getRenderer(Class<? extends IBionicPart> partClass) {
        return rendererMap.get(partClass);
    }
}

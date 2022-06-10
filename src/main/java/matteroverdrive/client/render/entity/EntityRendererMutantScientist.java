
package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import matteroverdrive.client.model.ModelHulkingScientist;
import matteroverdrive.entity.monster.EntityMutantScientist;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class EntityRendererMutantScientist extends RenderBiped<EntityMutantScientist> {

	private final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTITIES + "hulking_scinetist.png");

	public EntityRendererMutantScientist(RenderManager renderManager) {
		super(renderManager, new ModelHulkingScientist(), 0);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMutantScientist entity) {
		return texture;
	}

}

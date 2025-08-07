package io.github.yo56789.toywatergun.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;

@Environment(EnvType.CLIENT)
public class ProjectileModel extends EntityModel<ProjectileEntityRenderState> {

	public ProjectileModel(ModelPart root) {
        super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		modelPartData.addChild("main", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.rotation(0.0F, 0.0F, 0.0F));

		return TexturedModelData.of(modelData, 16, 16);
	}
}
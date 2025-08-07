package io.github.yo56789.toywatergun.client;

import io.github.yo56789.toywatergun.ToyWaterGun;
import io.github.yo56789.toywatergun.client.model.ProjectileModel;
import io.github.yo56789.toywatergun.entity.ProjectileBase;
import io.github.yo56789.toywatergun.entity.WaterProjectile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class ProjectileRenderer extends EntityRenderer<ProjectileBase, ProjectileEntityRenderState> {
    private final ProjectileModel model;
    public static final Identifier TEXTURE = Identifier.of(ToyWaterGun.MOD_ID, "textures/entity/projectile.png");

    public ProjectileRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new ProjectileModel(context.getPart(TWGClient.PROJECTILE_LAYER));
    }

    @Override
    public void render(ProjectileEntityRenderState state, MatrixStack stack, VertexConsumerProvider provider, int i) {
        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw - 90.0F));
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.pitch));
        VertexConsumer consumer = provider.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
        this.model.setAngles(state);
        this.model.render(stack, consumer, i, OverlayTexture.DEFAULT_UV);
        stack.pop();
        super.render(state, stack, provider, i);
    }

    @Override
    public void updateRenderState(ProjectileBase projectile, ProjectileEntityRenderState state, float f) {
        super.updateRenderState(projectile, state, f);
        state.pitch = projectile.getLerpedPitch(f);
        state.yaw = projectile.getLerpedYaw(f);
        state.shake = 0;
    }

    @Override
    public ProjectileEntityRenderState createRenderState() {
        return new ProjectileEntityRenderState();
    }
}

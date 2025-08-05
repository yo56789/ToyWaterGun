package io.github.yo56789.toywatergun.client.model;

import io.github.yo56789.toywatergun.item.TWGItems;
import io.github.yo56789.toywatergun.item.WaterGunItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)
public class WaterGunRenderer extends GeoItemRenderer<WaterGunItem> {
    public WaterGunRenderer() {
        super(new WaterGunModel());
    }

    @Override
    public void addRenderData(WaterGunItem animatable, RenderData relatedObject, GeoRenderState renderState) {
        renderState.addGeckolibData(WaterGunItem.GAUGE_DEGREES, relatedObject.itemStack().getOrDefault(TWGItems.FLUID_COMPONENT, 0));
    }

    @Override
    public void preRender(GeoRenderState renderState, MatrixStack poseStack, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, int packedLight, int packedOverlay, int renderColor) {
        model.getBone("Gauge").ifPresent(geoBone -> geoBone.setRotX(calcRot(renderState.getOrDefaultGeckolibData(WaterGunItem.GAUGE_DEGREES, 0))));

        super.preRender(renderState, poseStack, model, bufferSource, buffer, isReRender, packedLight, packedOverlay, renderColor);
    }

    private float calcRot(int remaining) {
        float percent = (float) remaining / WaterGunItem.MAX_FLUID;
        float deg = percent * 80 - 40;

        return Math.toRadians(deg);
    }
}

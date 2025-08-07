package io.github.yo56789.toywatergun.client;

import io.github.yo56789.toywatergun.ToyWaterGun;
import io.github.yo56789.toywatergun.client.model.TankRenderLayer;
import io.github.yo56789.toywatergun.client.model.TranslucentRenderLayer;
import io.github.yo56789.toywatergun.client.model.WaterGunModel;
import io.github.yo56789.toywatergun.item.TWGItems;
import io.github.yo56789.toywatergun.item.WaterGunItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)
public class WaterGunRenderer extends GeoItemRenderer<WaterGunItem> {
    public WaterGunRenderer() {
        super(new WaterGunModel());

        addRenderLayer(new TankRenderLayer<>(this, Identifier.of(ToyWaterGun.MOD_ID, "textures/transparent.png")));
        addRenderLayer(new TranslucentRenderLayer<>(this, Identifier.of(ToyWaterGun.MOD_ID, "textures/item/tank.png")));
    }

    @Override
    public void addRenderData(WaterGunItem animatable, RenderData relatedObject, GeoRenderState renderState) {
        renderState.addGeckolibData(WaterGunItem.GAUGE_DEGREES, relatedObject.itemStack().getOrDefault(TWGItems.CHARGE_COMPONENT, 0));
        renderState.addGeckolibData(WaterGunItem.FLUID, relatedObject.itemStack().getOrDefault(TWGItems.FLUID_COMPONENT, TWGItems.DEFAULT_FLUID_COMPONENT));
    }

    @Override
    public void preApplyRenderLayers(GeoRenderState renderState, MatrixStack poseStack, BakedGeoModel model, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {
        model.getBone("Gauge").ifPresent(gauge -> gauge.setRotX(calcRot(renderState.getOrDefaultGeckolibData(WaterGunItem.GAUGE_DEGREES, 0))));
        model.getBone("FluidLevel").ifPresent(fluid -> fluid.setScaleY(calcHeight(renderState.getOrDefaultGeckolibData(WaterGunItem.FLUID, TWGItems.DEFAULT_FLUID_COMPONENT).mb())));

        super.preApplyRenderLayers(renderState, poseStack, model, renderType, bufferSource, buffer, packedLight, packedOverlay, renderColor);
    }

    private float calcRot(int remaining) {
        float percent = (float) remaining / WaterGunItem.MAX_CHARGE;
        float deg = percent * 80 - 40;

        return Math.toRadians(deg);
    }

    private float calcHeight(int level) {
        return Math.clamp((float) level / WaterGunItem.MAX_FLUID, 0, 1);
    }
}
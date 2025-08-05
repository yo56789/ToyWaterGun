package io.github.yo56789.toywatergun.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.layer.CustomBoneTextureGeoLayer;

@Environment(EnvType.CLIENT)
public class TranslucentRenderLayer<T extends GeoAnimatable, O, R extends GeoRenderState> extends CustomBoneTextureGeoLayer<T, O, R> {
    public TranslucentRenderLayer(GeoRenderer<T, O, R> renderer, String boneName, Identifier texture) {
        super(renderer, boneName, texture);
    }

    @Override
    protected RenderLayer getRenderType(R renderState, Identifier texture) {
        return RenderLayer.getItemEntityTranslucentCull(texture);
    }
}

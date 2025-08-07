package io.github.yo56789.toywatergun.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.layer.TextureLayerGeoLayer;

@Environment(EnvType.CLIENT)
public class TranslucentRenderLayer<T extends GeoAnimatable, O, R extends GeoRenderState> extends TextureLayerGeoLayer<T, O, R> {

    public TranslucentRenderLayer(GeoRenderer<T, O, R> renderer, Identifier texture) {
        super(renderer, texture, RenderLayer::getItemEntityTranslucentCull);
    }
}
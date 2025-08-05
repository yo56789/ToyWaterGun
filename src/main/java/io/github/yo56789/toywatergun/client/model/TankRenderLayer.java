package io.github.yo56789.toywatergun.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;

@Environment(EnvType.CLIENT)
public class TankRenderLayer<T extends GeoAnimatable, O, R extends GeoRenderState> extends TranslucentRenderLayer<T, O, R> {

    public TankRenderLayer(GeoRenderer<T, O, R> renderer, String boneName, Identifier texture) {
        super(renderer, boneName, texture);
    }

    @Override
    protected Identifier getTextureResource(R state) {
        // Change based on fluid from renderState
        return super.getTextureResource(state);
    }
}
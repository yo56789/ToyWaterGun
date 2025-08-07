package io.github.yo56789.toywatergun.client.model;

import io.github.yo56789.toywatergun.ToyWaterGun;
import io.github.yo56789.toywatergun.item.FluidComponent;
import io.github.yo56789.toywatergun.item.TWGItems;
import io.github.yo56789.toywatergun.item.WaterGunItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;

public class TankRenderLayer<T extends GeoAnimatable, O, R extends GeoRenderState> extends TranslucentRenderLayer<T, O, R> {
    public TankRenderLayer(GeoRenderer<T, O, R> renderer, Identifier texture) {
        super(renderer, texture);
    }

    @Override
    protected Identifier getTextureResource(R state) {
        FluidComponent fluid = state.getOrDefaultGeckolibData(WaterGunItem.FLUID, TWGItems.DEFAULT_FLUID_COMPONENT);

        if (fluid.mb() > 0) {
            return Identifier.of(ToyWaterGun.MOD_ID, "textures/item/" + fluid.id() + ".png");
        }

        return this.texture;
    }
}

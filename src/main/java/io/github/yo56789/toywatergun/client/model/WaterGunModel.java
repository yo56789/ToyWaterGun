package io.github.yo56789.toywatergun.client.model;

import io.github.yo56789.toywatergun.ToyWaterGun;
import io.github.yo56789.toywatergun.item.WaterGunItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Environment(EnvType.CLIENT)
public class WaterGunModel extends GeoModel<WaterGunItem> {
    private final Identifier model = Identifier.of(ToyWaterGun.MOD_ID, "water_gun");
    private final Identifier animations = Identifier.of(ToyWaterGun.MOD_ID, "water_gun");
    private final Identifier texture = Identifier.of(ToyWaterGun.MOD_ID, "textures/item/water_gun.png");

    @Override
    public @Nullable RenderLayer getRenderType(GeoRenderState renderState, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    public Identifier getModelResource(GeoRenderState geoRenderState) {
        return this.model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState geoRenderState) {
        return this.texture;
    }

    @Override
    public Identifier getAnimationResource(WaterGunItem waterGunItem) {
        return this.animations;
    }
}

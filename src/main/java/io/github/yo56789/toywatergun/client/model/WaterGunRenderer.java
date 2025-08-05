package io.github.yo56789.toywatergun.client.model;

import io.github.yo56789.toywatergun.item.WaterGunItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@Environment(EnvType.CLIENT)
public class WaterGunRenderer extends GeoItemRenderer<WaterGunItem> {
    public WaterGunRenderer() {
        super(new WaterGunModel());
    }
}

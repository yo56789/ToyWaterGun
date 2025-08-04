package io.github.yo56789.toywatergun.client.model;

import io.github.yo56789.toywatergun.item.WaterGunItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class WaterGunRenderer extends GeoItemRenderer<WaterGunItem> {
    public WaterGunRenderer() {
        super(new WaterGunModel());
    }
}

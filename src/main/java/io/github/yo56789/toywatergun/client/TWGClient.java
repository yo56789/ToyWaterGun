package io.github.yo56789.toywatergun.client;

import io.github.yo56789.toywatergun.entity.TWGEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class TWGClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(TWGEntities.WATER_PROJECTILE_TYPE, WaterProjectileRenderer::new);
    }
}

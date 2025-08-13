package io.github.yo56789.toywatergun.client;

import io.github.yo56789.toywatergun.ToyWaterGun;
import io.github.yo56789.toywatergun.client.model.ProjectileModel;
import io.github.yo56789.toywatergun.entity.TWGEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TWGClient implements ClientModInitializer {
    public static final EntityModelLayer PROJECTILE_LAYER = new EntityModelLayer(Identifier.of(ToyWaterGun.MOD_ID, "projectile"), "main");


    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(TWGEntities.WATER_PROJECTILE_TYPE, ProjectileRenderer::new);
        EntityRendererRegistry.register(TWGEntities.LAVA_PROJECTILE_TYPE, ProjectileRenderer::new);
        EntityRendererRegistry.register(TWGEntities.SNOW_PROJECTILE_TYPE, ProjectileRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(PROJECTILE_LAYER, ProjectileModel::getTexturedModelData);
    }
}

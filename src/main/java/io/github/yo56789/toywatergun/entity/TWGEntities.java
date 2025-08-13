package io.github.yo56789.toywatergun.entity;

import io.github.yo56789.toywatergun.ToyWaterGun;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class TWGEntities {
    public static final EntityType<WaterProjectile> WATER_PROJECTILE_TYPE = register("water_projectile", EntityType.Builder.<WaterProjectile>create(WaterProjectile::new, SpawnGroup.MISC).dropsNothing().dimensions(0.25f, 0.25f).maxTrackingRange(4).trackingTickInterval(10).makeFireImmune());
    public static final EntityType<LavaProjectile> LAVA_PROJECTILE_TYPE = register("lava_projectile", EntityType.Builder.<LavaProjectile>create(LavaProjectile::new, SpawnGroup.MISC).dropsNothing().dimensions(0.25f, 0.25f).maxTrackingRange(4).trackingTickInterval(10).makeFireImmune());
    public static final EntityType<SnowProjectile> SNOW_PROJECTILE_TYPE = register("snow_projectile", EntityType.Builder.<SnowProjectile>create(SnowProjectile::new, SpawnGroup.MISC).dropsNothing().dimensions(0.25f, 0.25f).maxTrackingRange(4).trackingTickInterval(10).makeFireImmune());

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(ToyWaterGun.MOD_ID, id));

        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

    public static void init() {

    }
}

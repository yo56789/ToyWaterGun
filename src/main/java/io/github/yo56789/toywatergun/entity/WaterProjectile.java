package io.github.yo56789.toywatergun.entity;

import io.github.yo56789.toywatergun.ToyWaterGun;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class WaterProjectile extends ProjectileBase {

    public WaterProjectile(EntityType<? extends WaterProjectile> entityType, World world) {
        super(entityType, world);
    }

    public WaterProjectile(World world, double x, double y, double z) {
        super(TWGEntities.WATER_PROJECTILE_TYPE, world, x, y,z);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 3) {
            // particle stuff on destroy
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        super.onEntityHit(hitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            this.getWorld().sendEntityStatus(this, (byte) 3);
            this.discard();
        }

        super.onBlockHit(hitResult);
    }
}

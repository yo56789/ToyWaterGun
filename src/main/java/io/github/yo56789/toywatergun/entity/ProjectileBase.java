package io.github.yo56789.toywatergun.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class ProjectileBase extends ProjectileEntity {
    public ProjectileBase(EntityType<? extends ProjectileBase> entityType, World world) {
        super(entityType, world);
    }

    public ProjectileBase(EntityType<? extends ProjectileBase> entityType, World world, double x, double y, double z) {
        super(entityType, world);
        this.setPosition(x, y, z);
    }

    @Override
    public boolean canUsePortals(boolean a) {
        return false;
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 256;
    }

    @Override
    public void tick() {
        this.applyGravity();
        this.setVelocity(this.getVelocity().multiply(0.9));

        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        Vec3d pos = hitResult.getType() != HitResult.Type.MISS ? hitResult.getPos() : this.getPos().add(this.getVelocity());

        this.setPosition(pos);
        this.updateRotation();
        this.tickBlockCollision();
        super.tick();

        if (hitResult.getType() != HitResult.Type.MISS && this.isAlive()) {
            this.onCollision(hitResult);
        } else if (this.getWorld().isClient()) {
            spawnParticles(this.getVelocity());
        }
    }

    @Override
    protected double getGravity() {
        return 0.03;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    protected abstract void spawnParticles(Vec3d velo);

    public abstract double getMultiplier();
}

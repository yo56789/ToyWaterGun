package io.github.yo56789.toywatergun.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.TrailParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Colors;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class SnowProjectile extends ProjectileBase {
    public SnowProjectile(EntityType<? extends SnowProjectile> entityType, World world) {
        super(entityType, world);
    }

    public SnowProjectile(World world, double x, double y, double z) {
        super(TWGEntities.SNOW_PROJECTILE_TYPE, world, x, y, z);
    }

    @Override
    protected void spawnParticles(Vec3d velo) {
        for (int i = 0; i < 10; i++) {
            this.getWorld().addImportantParticleClient(new TrailParticleEffect(this.getPos(), Colors.WHITE, 20), true, this.getParticleX(1), this.getRandomBodyY(), this.getParticleZ(1), velo.getX() / 4, velo.getY() / 4, velo.getZ() / 4);
        }
    }

    @Override
    public double getMultiplier() {
        return 0.2;
    }

    @Override
    protected double getGravity() {
        return 0.06;
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        if (!this.getWorld().isClient()) {
            Entity entity = hitResult.getEntity();

            entity.damage((ServerWorld) this.getWorld(), this.getDamageSources().indirectMagic(this, this.getOwner()), 6);

            this.discard();
        }

        super.onEntityHit(hitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        if (!this.getWorld().isClient() && hasPermission()) {
            if (hasPermission()) {
                boolean hit = this.setBlock(hitResult.getBlockPos());
                if (!hit) {
                    this.setBlock(hitResult.getBlockPos().offset(hitResult.getSide()));
                }
            }

            this.getWorld().playSound(null, hitResult.getBlockPos(), SoundEvents.BLOCK_SNOW_HIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
            this.discard();
        }

        super.onBlockHit(hitResult);
    }

    private boolean setBlock(BlockPos pos) {
        World world = this.getWorld();
        if (world.isInBuildLimit(pos) && world.isAir(pos)) {
            world.setBlockState(pos, Blocks.POWDER_SNOW.getDefaultState());
            world.emitGameEvent(this.getOwner(), GameEvent.FLUID_PLACE, pos);

            return true;
        }

        return false;
    }
}

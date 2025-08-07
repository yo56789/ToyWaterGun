package io.github.yo56789.toywatergun.entity;

import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.TrailParticleEffect;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LavaProjectile extends ProjectileBase {
    private static final int ORANGE_COLOR = ColorHelper.fromFloats(1f, 1f, 0.48f, 0);

    public LavaProjectile(EntityType<? extends ProjectileBase> entityType, World world) {
        super(entityType, world);
    }

    public LavaProjectile(World world, double x, double y, double z) {
        super(TWGEntities.LAVA_PROJECTILE_TYPE, world, x, y, z);
    }

    @Override
    protected void spawnParticles(Vec3d velo) {
        for (int i = 0; i < 10; i++) {
            this.getWorld().addParticleClient(new TrailParticleEffect(this.getPos(), ORANGE_COLOR, 20), this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), velo.getX() / 3, velo.getY() / 3, velo.getZ() / 3);
        }
    }

    @Override
    public double getMultiplier() {
        return 0.2;
    }

    @Override
    protected double getGravity() {
        return 0.05;
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        if (!this.getWorld().isClient()) {
            Entity entity = hitResult.getEntity();

            if (!entity.isFireImmune()) {
                entity.setOnFireForTicks(30);
            }
            entity.damage((ServerWorld) this.getWorld(), this.getDamageSources().indirectMagic(this, this.getOwner()), 4);

            this.discard();
        }

        super.onEntityHit(hitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        if (!this.getWorld().isClient()) {
            boolean hit = this.setFire(hitResult.getBlockPos(), hitResult.getSide());
            if (!hit) {
                this.setFire(hitResult.getBlockPos().offset(hitResult.getSide()), hitResult.getSide());
            }

            this.discard();
        }

        super.onBlockHit(hitResult);
    }

    private boolean setFire(BlockPos pos, Direction dir) {
        BlockState state = this.getWorld().getBlockState(pos);

        if (!AbstractCandleBlock.isLitCandle(state) && state.isIn(BlockTags.CANDLES)) {
            this.getWorld().setBlockState(pos, state.with(AbstractCandleBlock.LIT, true));
            return true;
        } else if (!CampfireBlock.isLitCampfire(state) && state.isIn(BlockTags.CAMPFIRES)) {
            this.getWorld().setBlockState(pos, state.with(CampfireBlock.LIT, true));
            return true;
        } else if (AbstractFireBlock.canPlaceAt(this.getWorld(), pos, dir)) {
            this.getWorld().setBlockState(pos, AbstractFireBlock.getState(this.getWorld(), pos), 11);
            return true;
        }

        return false;
    }
}

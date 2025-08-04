package io.github.yo56789.toywatergun.entity;

import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
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
        if (!this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, (byte) 3);

            this.extinguishFire(hitResult.getBlockPos());
            this.extinguishFire(hitResult.getBlockPos().offset(hitResult.getSide()));

            this.discard();
        }

        super.onBlockHit(hitResult);
    }

    private void extinguishFire(BlockPos pos) {
        BlockState state = this.getWorld().getBlockState(pos);

        if (state.isIn(BlockTags.FIRE)) {
            this.getWorld().breakBlock(pos, false, this);
        } else if (AbstractCandleBlock.isLitCandle(state)) {
            AbstractCandleBlock.extinguish(null, state, this.getWorld(), pos);
        } else if (CampfireBlock.isLitCampfire(state)) {
            // eventId = magic number found in PotionEntity
            // Plays campfire extinguish sound (WorldEventHandler)
            this.getWorld().syncWorldEvent(null, 1009, pos, 0);
            CampfireBlock.extinguish(this.getOwner(), this.getWorld(), pos, state);
            this.getWorld().setBlockState(pos, state.with(CampfireBlock.LIT, false));
        }
    }
}

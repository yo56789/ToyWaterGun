package io.github.yo56789.toywatergun.item;

import io.github.yo56789.toywatergun.client.WaterGunRenderer;
import io.github.yo56789.toywatergun.entity.LavaProjectile;
import io.github.yo56789.toywatergun.entity.WaterProjectile;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class WaterGunItem extends Item implements GeoItem {

    private static final RawAnimation ACTIVATE_ANIM = RawAnimation.begin().thenPlay("water_gun.charge");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final DataTicket<Integer> GAUGE_DEGREES = DataTicket.create("gauge_degrees", Integer.class);
    public static final DataTicket<FluidComponent> FLUID = DataTicket.create("fluid", FluidComponent.class);
    public static final int MAX_CHARGE = 20;
    public static final int MAX_FLUID = 1000;

    public WaterGunItem(Settings settings) {
        super(settings);

        GeoItem.registerSyncedAnimatable(this);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) {
            return ActionResult.CONSUME;
        }

        ItemStack stack = player.getStackInHand(hand);
        if (player.getItemCooldownManager().isCoolingDown(stack)) {
            return ActionResult.FAIL;
        }

        int charge = stack.getOrDefault(TWGItems.CHARGE_COMPONENT, 0);
        FluidComponent fluid = stack.getOrDefault(TWGItems.FLUID_COMPONENT, TWGItems.DEFAULT_FLUID_COMPONENT);

        // Pressure increase
        if (player.isSneaking()) {
            if (stack.getOrDefault(TWGItems.CHARGE_COMPONENT,0) == MAX_CHARGE) {
                return ActionResult.SUCCESS;
            }

            stack.set(TWGItems.CHARGE_COMPONENT, Math.clamp(charge + 4, 0, MAX_CHARGE));
            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerWorld) world), "Activation", "charge");
            player.getItemCooldownManager().set(stack, 45);

            return ActionResult.SUCCESS;
        }

        // Fire fluid
        if (fluid.mb() >= 25) {
            stack.set(TWGItems.FLUID_COMPONENT, new FluidComponent(fluid.id(), fluid.mb() - 25));
            stack.set(TWGItems.CHARGE_COMPONENT, Math.clamp(charge - 1, 0, MAX_CHARGE));

            if (fluid.id().equals("lava")) {
                LavaProjectile projectile = new LavaProjectile(world, player.getX(), player.getEyeY() - 0.10000000149011612, player.getZ());
                projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, (float) (projectile.getMultiplier() * ((double) charge / 4) + 0.1), 0.5F);
                projectile.setOwner(player);

                world.spawnEntity(projectile);
            } else if (fluid.id().equals("water")) {
                WaterProjectile projectile = new WaterProjectile(world, player.getX(), player.getEyeY() - 0.10000000149011612, player.getZ());
                projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, (float) (projectile.getMultiplier() * ((double) charge / 4) + 0.1), 0.5F);
                projectile.setOwner(player);

                world.spawnEntity(projectile);
            }

            player.getItemCooldownManager().set(stack, 5);

            return ActionResult.SUCCESS;
        }

        // Bucket style refill
        BlockHitResult hitResult = raycast(world, player, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();
            if (world.canEntityModifyAt(player, blockPos) && player.canPlaceOn(blockPos.offset(hitResult.getSide()), hitResult.getSide(), stack)) {
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() instanceof FluidDrainable source) {
                    ItemStack fluidType = source.tryDrainFluid(player, world, blockPos, state);
                    if (!fluidType.isEmpty()) {
                        if (fluidType.isOf(Items.WATER_BUCKET)) {
                            stack.set(TWGItems.FLUID_COMPONENT, new FluidComponent("water", 1000));
                        } else if (fluidType.isOf(Items.LAVA_BUCKET)) {
                            stack.set(TWGItems.FLUID_COMPONENT, new FluidComponent("lava", 1000));
                        } else if (fluidType.isOf(Items.POWDER_SNOW_BUCKET)) {
                            stack.set(TWGItems.FLUID_COMPONENT, new FluidComponent("snow", 1000));
                        }

                        source.getBucketFillSound().ifPresent((sound) -> player.playSound(sound, 1.0f, 1.0f));
                        world.emitGameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
                        return ActionResult.SUCCESS;
                    }
                }
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private WaterGunRenderer renderer;

            @Override
            public GeoItemRenderer<?> getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new WaterGunRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("Activation", 0, animTest -> PlayState.STOP).triggerableAnim("charge", ACTIVATE_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}

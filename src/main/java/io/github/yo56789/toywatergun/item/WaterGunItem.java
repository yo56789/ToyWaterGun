package io.github.yo56789.toywatergun.item;

import io.github.yo56789.toywatergun.client.model.WaterGunRenderer;
import io.github.yo56789.toywatergun.entity.WaterProjectile;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
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
    public static final DataTicket<Integer> TANK_LEVEL = DataTicket.create("tank_level", Integer.class);
    public static final int MAX_CHARGE = 20;
    public static final int MAX_FLUID = 1000;

    public WaterGunItem(Settings settings) {
        super(settings);

        GeoItem.registerSyncedAnimatable(this);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) {
            // No arm swing when
            return ActionResult.CONSUME;
        }

        ItemStack stack = player.getStackInHand(hand);
        int charge = stack.getOrDefault(TWGItems.CHARGE_COMPONENT, 0);
        int fluid = stack.getOrDefault(TWGItems.FLUID_COMPONENT, 0);

        if (player.isSneaking() && !player.getItemCooldownManager().isCoolingDown(stack)) {
            if (stack.getOrDefault(TWGItems.CHARGE_COMPONENT,0) == MAX_CHARGE) {
                return ActionResult.SUCCESS;
            }

            stack.set(TWGItems.CHARGE_COMPONENT, Math.clamp(charge + 4, 0, MAX_CHARGE));
            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerWorld) world), "Activation", "charge");
            player.getItemCooldownManager().set(stack, 45);

            return ActionResult.SUCCESS;
        }

        if (!player.getItemCooldownManager().isCoolingDown(stack) && fluid >= 25) {
            stack.set(TWGItems.FLUID_COMPONENT, fluid - 25);
            stack.set(TWGItems.CHARGE_COMPONENT, Math.clamp(charge - 1, 0, MAX_CHARGE));

            WaterProjectile projectile = new WaterProjectile(world, player.getX(), player.getEyeY() - 0.10000000149011612, player.getZ());
            projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, (float) (0.35 * (charge / 4) + 0.1), 0.5f);

            world.spawnEntity(projectile);

            player.getItemCooldownManager().set(stack, 5);

            return ActionResult.SUCCESS;
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        int charge = stack.getOrDefault(TWGItems.CHARGE_COMPONENT, 0);
        int remaining = stack.getOrDefault(TWGItems.FLUID_COMPONENT, 0);

        textConsumer.accept(Text.translatable("item.toywatergun.water_gun.charge", charge));
        textConsumer.accept(Text.translatable("item.toywatergun.water_gun.remaining", remaining));
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

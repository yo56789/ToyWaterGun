package io.github.yo56789.toywatergun.item;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class WaterGunItem extends Item {
    public WaterGunItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        ItemStack stack = player.getStackInHand(hand);
        if (player.isSneaking() && !player.getItemCooldownManager().isCoolingDown(stack)) {
            stack.set(Items.CHARGE_COMPONENT, stack.getOrDefault(Items.CHARGE_COMPONENT, 0) + 1);
            player.getItemCooldownManager().set(stack, 200);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        int charge = stack.getOrDefault(Items.CHARGE_COMPONENT, 0);
        int remaining = stack.getOrDefault(Items.FLUID_COMPONENT, 0);

        textConsumer.accept(Text.translatable("item.toywatergun.water_gun.charge", charge));
        textConsumer.accept(Text.translatable("item.toywatergun.water_gun.remaining", remaining));
    }
}

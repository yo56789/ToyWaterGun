package io.github.yo56789.toywatergun.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.yo56789.toywatergun.item.FluidComponent;
import io.github.yo56789.toywatergun.item.TWGItems;
import io.github.yo56789.toywatergun.item.WaterGunItem;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Objects;

@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorMixin {
	@Inject(method = "registerBehavior", at = @At("TAIL"))
    private static void registerBehavior(CallbackInfo ci, @Local(ordinal=1) Map<Item, CauldronBehavior> water, @Local(ordinal=2) Map<Item, CauldronBehavior> lava) {
		water.put(TWGItems.WATER_GUN, (state, world, pos, player, hand, stack) -> {
			if (!world.isClient()) {
				FluidComponent fluid = stack.getOrDefault(TWGItems.FLUID_COMPONENT, TWGItems.DEFAULT_FLUID_COMPONENT);

				if ((fluid.mb() < WaterGunItem.MAX_FLUID && Objects.equals(fluid.id(), "water")) || fluid.mb() == 0) {
					stack.set(TWGItems.FLUID_COMPONENT, new FluidComponent("water", Math.clamp(fluid.mb() + 250, 0, WaterGunItem.MAX_FLUID)));

					LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
					world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
			}

			return ActionResult.SUCCESS;
		});
		lava.put(TWGItems.WATER_GUN, (state, world, pos, player, hand, stack) -> {
			if (!world.isClient()) {
				FluidComponent fluid = stack.getOrDefault(TWGItems.FLUID_COMPONENT, TWGItems.DEFAULT_FLUID_COMPONENT);

				if ((fluid.mb() < WaterGunItem.MAX_FLUID && Objects.equals(fluid.id(), "lava")) || fluid.mb() == 0) {
					stack.set(TWGItems.FLUID_COMPONENT, new FluidComponent("lava", 1000));

					world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
					world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
			}

			return ActionResult.SUCCESS;
		});
	}
}
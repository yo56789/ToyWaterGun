package io.github.yo56789.toywatergun.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.yo56789.toywatergun.item.ToyWaterGunItems;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorMixin {
	@Inject(method = "registerBehavior", at = @At("TAIL"))
    private static void registerBehavior(CallbackInfo ci, @Local(ordinal=1) Map<Item, CauldronBehavior> map2) {
		map2.put(ToyWaterGunItems.WATER_GUN, (state, world, pos, player, hand, stack) -> {
			if (!world.isClient()) {
				int fluid = stack.getOrDefault(ToyWaterGunItems.FLUID_COMPONENT, 0);
				if (fluid < 1000) {
					stack.set(ToyWaterGunItems.FLUID_COMPONENT, Math.clamp(fluid + 250, 0, 1000));
					LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
				}
			}

			return ActionResult.SUCCESS;
		});
	}
}
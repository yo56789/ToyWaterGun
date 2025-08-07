package io.github.yo56789.toywatergun.item;

import com.mojang.serialization.Codec;
import io.github.yo56789.toywatergun.ToyWaterGun;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class TWGItems {

    public static final WaterGunItem WATER_GUN = register("water_gun", WaterGunItem::new, new Item.Settings().maxCount(1));

    public static final ComponentType<Integer> CHARGE_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(ToyWaterGun.MOD_ID, "charge"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<FluidComponent> FLUID_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(ToyWaterGun.MOD_ID, "fluid"),
            ComponentType.<FluidComponent>builder().codec(FluidComponent.CODEC).build()
    );

    public static final FluidComponent DEFAULT_FLUID_COMPONENT = new FluidComponent("water", 0);

    public static <I extends Item> I register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ToyWaterGun.MOD_ID, name));

        Item item = itemFactory.apply(settings.registryKey(itemKey));

        Registry.register(Registries.ITEM, itemKey, item);

        return (I) item;
    }



    public static void init() {
        final ItemGroup GROUP = FabricItemGroup.builder()
                .icon(() -> new ItemStack(WATER_GUN))
                .displayName(Text.translatable("itemGroup.toywatergun"))
                .entries((ctx, entr) -> {
                    entr.add(WATER_GUN);
                })
                .build();

        Registry.register(Registries.ITEM_GROUP, Identifier.of(ToyWaterGun.MOD_ID, "group"), GROUP);
    }
}

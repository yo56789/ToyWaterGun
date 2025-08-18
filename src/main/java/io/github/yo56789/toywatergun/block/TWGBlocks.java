package io.github.yo56789.toywatergun.block;

import io.github.yo56789.toywatergun.ToyWaterGun;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class TWGBlocks {
    public static final CaseBlock CASE_BLOCK = register("case", CaseBlock::new, AbstractBlock.Settings.create().strength(2.0f, 2.0f).sounds(BlockSoundGroup.WOOD), new Item.Settings().maxCount(1));

    public static <I extends Block> I register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings blockSettings, Item.Settings itemSettings) {
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(ToyWaterGun.MOD_ID, name));
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(ToyWaterGun.MOD_ID, name));

        Block block = blockFactory.apply(blockSettings.registryKey(blockKey));
        Registry.register(Registries.BLOCK, blockKey, block);
        Registry.register(Registries.ITEM, itemKey, new BlockItem(block, itemSettings.registryKey(itemKey)));

        return (I) block;
    }

    public static void init() {

    }
}

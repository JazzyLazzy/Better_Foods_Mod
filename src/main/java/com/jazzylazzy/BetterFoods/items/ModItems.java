package com.jazzylazzy.BetterFoods.items;

import com.jazzylazzy.BetterFoods.BetterFoods;
import com.jazzylazzy.BetterFoods.blocks.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final Item RICE_CROP = registerItem("rice_crop",
            new Item(new FabricItemSettings()
                    .group(ItemGroup.MATERIALS)));

    public static final Item UNCOOKED_RICE = registerItem("uncooked_rice",
            new AliasedBlockItem(ModBlocks.RICE_CROP_BLOCK, new FabricItemSettings()
                    .group(ItemGroup.FOOD)));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(BetterFoods.MOD_ID, name), item);
    }

    public static void registerModItems(){
        System.out.println("registering items");
        BetterFoods.LOGGER.debug("Registering items for: " + BetterFoods.MOD_ID);
    }

}

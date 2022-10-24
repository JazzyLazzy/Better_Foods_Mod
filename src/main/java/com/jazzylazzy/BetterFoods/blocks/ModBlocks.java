package com.jazzylazzy.BetterFoods.blocks;

import com.jazzylazzy.BetterFoods.BetterFoods;
import com.jazzylazzy.BetterFoods.CropBlocks.EggplantCropBlock;
import com.jazzylazzy.BetterFoods.CropBlocks.RiceCropBlock;
import com.jazzylazzy.BetterFoods.CropBlocks.Tall_RiceCropBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block EGGPLANT_CORP_BLOCK = registerBlock("eggplant_crop_block",
            new EggplantCropBlock(FabricBlockSettings.copy(Blocks.WHEAT)), ItemGroup.BUILDING_BLOCKS, false);

    public static final Block FLOODED_FARMLAND = registerBlock("flooded_farmland",
            new WateredFarmlandBlock(FabricBlockSettings.copy(Blocks.FARMLAND)), ItemGroup.BUILDING_BLOCKS, false);

    public static final Block RICE_CROP_BLOCK = registerBlock("rice_crop_block",
            new RiceCropBlock(FabricBlockSettings.copy(Blocks.WHEAT)), ItemGroup.BUILDING_BLOCKS, false);

    public static final Block TALL_RICE_CROP_BLOCK = registerBlock("tall_rice_crop_block",
            new Tall_RiceCropBlock(FabricBlockSettings.copy(Blocks.WHEAT)), ItemGroup.BUILDING_BLOCKS, false);

    public static final Block RICE_COOKER = registerBlock("rice_cooker",
            new RiceCookerBlock(FabricBlockSettings.copy(Blocks.FURNACE)), ItemGroup.BUILDING_BLOCKS, true);

    private static Block registerBlock(String name, Block block, ItemGroup tab, boolean hasItem) {
        if (hasItem){
            registerBlockItem(name, block, tab);
        }
        return Registry.register(Registry.BLOCK, new Identifier(BetterFoods.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block, ItemGroup tab) {
        Registry.register(Registry.ITEM, new Identifier(BetterFoods.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlocks() {
        BetterFoods.LOGGER.debug("Registering ModBlocks for " + BetterFoods.MOD_ID);
    }

}

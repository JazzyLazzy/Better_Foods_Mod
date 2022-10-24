package com.jazzylazzy.BetterFoods.blocks.entities;

import com.jazzylazzy.BetterFoods.BetterFoods;
import com.jazzylazzy.BetterFoods.blocks.ModBlocks;
import com.jazzylazzy.BetterFoods.blocks.RiceCookerBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {

    public static BlockEntityType<RiceCookerEntity> RICE_COOKER = (BlockEntityType<RiceCookerEntity>) registerBlockEntity("rice_cooker",
            ModBlocks.RICE_COOKER, RiceCookerEntity::new);

    private static <T> BlockEntityType<? extends BlockEntity> registerBlockEntity(String name, Block block, FabricBlockEntityTypeBuilder.Factory<? extends BlockEntity> factory) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(BetterFoods.MOD_ID, name),
                FabricBlockEntityTypeBuilder.create(RiceCookerEntity::new,
                        block).build(null));
    }

    public static void registerModBlockEntities() {
        BetterFoods.LOGGER.debug("Registering Block Entities");
    }

}
